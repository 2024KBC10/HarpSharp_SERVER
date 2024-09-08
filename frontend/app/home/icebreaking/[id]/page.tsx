"use client"

import { useCallback, useEffect, useState } from "react"

import IBKDetailBottom from "./(component)/(bottom)"
import RegistAnswerModal from "./(component)/(registanswer)"
import IBKDetailTop from "./(component)/(top)"
import { APIManager } from "@/app/(common)/(api)"
import { 
    IceBreakingCommentDto, 
    IceBreakingCommentJson 
} from "../../../(common)/(interface)/icebreaking.dto"

import styles from "./ibk_detail.module.css"
import { IceBreakingCommentProvider } from "../../../(common)/(provider)/icebreaking.provider"

export default function IBKDetail({
    params,
    searchParams
}: IBKDetailProps) {
    const [showRegistAnswerModal, setShowRegistAnswerModal] = useState<boolean>(false)
    const [comments, setComments] = useState<IceBreakingCommentDto[]>([])
    const [data, setData] = useState<IIBKDetail>()

    const addComment = (comment: IceBreakingCommentDto) => setComments([comment, ...comments])

    const initComments = useCallback(async () => {
        if(Object.keys(searchParams).length > 0) {
            const comments_key = params.id
            await APIManager.get<IceBreakingCommentJson>({
                route: `/board/posts/${comments_key}/comments`
            })
            .then(res => {
                const arr: IceBreakingCommentDto[] = []
                if(typeof res !== "boolean") {
                    if("error" in res) return arr
                    const keys = Object.keys(res)
                    if(keys.length > 0) {
                        for(let i=0; i<keys.length; ++i) {
                            const key = keys[i]
                            arr.push(
                                IceBreakingCommentProvider
                                .toDto(key, res[key])
                            )
                        }
                    }
                }
                return arr
            })
            .then(comments => {
                setComments(comments)
                setData({
                    question_title: searchParams.title,
                    content: searchParams.content,
                    createdAt: new Date(searchParams.createdAt),
                })
            })
        }
    }, [params, searchParams])

    useEffect(() => { initComments() }, [initComments])

    return (
        <div className={styles.container}>
            <IBKDetailTop
            content={data ? data.content : "...loading"}
            createdAt={data ? new Date(data.createdAt) : new Date()}
            onShowModal={setShowRegistAnswerModal}/>
            <IBKDetailBottom replies={comments}/>
            {
                showRegistAnswerModal
                ?   <RegistAnswerModal
                    question_title={data?.question_title ?? "empty"}
                    question_id={params.id}
                    onAddComment={addComment}
                    onShowModal={setShowRegistAnswerModal} 
                    isShowing={showRegistAnswerModal}
                    />
                :   <></>
            }
        </div>
    )
}

interface IBKDetailProps {
    params: { id: string }
    searchParams: {
        title: string
        content: string
        createdAt: string
    }
}

interface IIBKDetail {
    question_title: string
    content: string
    createdAt: Date
}