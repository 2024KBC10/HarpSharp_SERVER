"use client"

import Image from "next/image"
import { useState } from "react"

import { 
    IceBreakingCommentDto, 
    IceBreakingCommentJson 
} from "../../../../../(common)/(interface)/icebreaking.dto"
import { APIManager } from "@/app/(common)/(api)"
import { LocalStorage } from "@/app/(common)/(storage)"
import Spacer from "@/app/(common)/(component)/(spacer)"
import PreviewAnswer from "./(component)/(previewanswer)"
import TextButton from "@/app/(common)/(component)/(button)"

import CloseIcon from "../../../../../../public/image/close.png"
import styles from "./regist_answer.module.css"

export default function RegistAnswerModal({
    question_title,
    question_id,
    isShowing,
    onAddComment,
    onShowModal
}: RegistAnswerModalProps) {
    const [selMemoColor, setSelMemoColor] = useState<number>(0)
    const [selPinColor, setSelPinColor] = useState<number>(0)
    const [input, setInput] = useState<string>("")

    const memoColors: MemoColor[] = ["yellow", "purple", "pink", "sky-blue"]
    const pinColors: PinColor[] = ["brown", "bright-green", "bright-yellow", "bright-pink"]

    const onChangeText = (value: string) => {if(value.length <= 50) setInput(value)}
    const registComment = async () => {
        const user = userModel.getUserData()
        if(input.length < 1) return
        else if(!user) {
            alert("로그인이 필요한 기능입니다")
            return
        }
        
        const token = LocalStorage.get("token")
        const result: IceBreakingCommentDto | string = await APIManager.post<IceBreakingCommentJson>({
            route: "/board/posts/comments",
            body: {
                postId: question_id,
                username: user.username,
                content: input,
                memoColor: memoColors[selMemoColor],
                pinColor: pinColors[selPinColor],
            },
            headers: { authorization: `${token}` }
        })
        .then(res => {
            if("error" in res) return res.error

            const data = res.data!
            const key = Object.keys(data)[0]
            return IceBreakingCommentProvider.toDto(key, data[key])
        })

        if(typeof result === "string") {
            alert(result)
            return
        }

        userModel.addLog(`아이스 브레이킹 ${question_title}에 답변을 등록했습니다.`)
        onShowModal(false)
        onAddComment(result)
    }

    return (
        <div 
        className={styles.container}
        style={{ display: isShowing ? "flex" : "none" }}
        >
            <Image
            src={CloseIcon}
            alt="닫기 버튼"
            width={12}
            height={12}
            className={styles.button}
            onClick={() => onShowModal(false)}
            />
            <span>답변 등록하기</span>
            <div className={styles.preview_container}>
                <PreviewAnswer 
                memo_color={getColor(memoColors[selMemoColor]) ?? "yellow"}
                pin_icon={getPinIconFromColor(pinColors[selPinColor]) ?? BrownPinIcon}
                text={input}
                />
                <input 
                value={input}
                onChange={(e) => onChangeText(e.target.value)}
                placeholder="답변을 입력해 주세요"
                />
            </div>
            <div className={styles.action_container}>
                <div className={styles.select_color_container}>
                    <div className={styles.select_color_wrapper_col}>
                        <span>메모지 색상</span>
                        <Spacer spacing={5} direction="column"/>
                        <div className={styles.select_color_wrapper_row}>
                            {
                                memoColors
                                .map((color, index) => 
                                    (<SelectMemoColor 
                                    key={index}
                                    color={color}
                                    selectIndex={selMemoColor}
                                    index={index}
                                    onChangeMemoColor={setSelMemoColor}
                                    />)
                                )
                            }
                        </div>
                    </div>
                    <Spacer spacing={10} direction="column"/>
                    <div className={styles.select_color_wrapper_col}>
                        <span>핀 색상</span>
                        <Spacer spacing={5} direction="column"/>
                        <div className={styles.select_color_wrapper_row}>
                            {
                                pinColors
                                .map((color, index) => 
                                    (<SelectMemoColor
                                    key={index} 
                                    color={color}
                                    selectIndex={selPinColor}
                                    index={index}
                                    onChangeMemoColor={setSelPinColor}
                                    />)
                                )
                            }
                        </div>
                    </div>
                </div>
                <div className={styles.button_wrapper}>
                    <TextButton
                    text="답변 등록"
                    type="blue"
                    onClick={registComment}
                    width={80}
                    height={20}
                    fontSize={14}
                    />
                </div>
            </div>
        </div>
    )
}

function SelectMemoColor({ 
    color,
    selectIndex,
    index,
    onChangeMemoColor,
}: Required<{ color: MemoColor | PinColor, selectIndex: number, index: number, onChangeMemoColor: (index: number) => void }>) {
    const memoColor = getColor(color)
    return (
        <div 
        className={styles.select_color}
        style={{ 
            backgroundColor: `${memoColor}`,
            boxShadow: `0px 4px 4px 0px rgba(0,0,0,${selectIndex === index ? .25 : 0})` 
        }}
        onClick={() => onChangeMemoColor(index)}
        />
    )
}

export function getColor(color: MemoColor | PinColor | string | null) {
    switch(color) {
        case "yellow": return "#FFFA5E"
        case "purple": return "#B45EFF"
        case "pink": return "#FF5EC4"
        case "sky-blue": return "#5EA3FF"  
        case "brown": return "#F5A953"
        case "bright-green": return "#E7FF5E"
        case "bright-pink": return "#FF5E5E"
        case "bright-yellow": return "#5EFF8E"
        default: return null
    }
}

import BrownPinIcon from "../../../../../../public/image/pin_default.png"
import BrightGreenPinIcon from "../../../../../../public/image/pin_bright_green.png"
import BrightPinkPinIcon from "../../../../../../public/image/pin_pink.png"
import BrightYellowPinIcon from "../../../../../../public/image/pin_bright_yellow.png"
import { IceBreakingCommentProvider } from "../../../../../(common)/(provider)/icebreaking.provider"
import { userModel } from "@/app/(common)/(model)"

export function getPinIconFromColor(color: PinColor | string | null) {
    switch(color) {
        case "brown": return BrownPinIcon
        case "bright-green": return BrightGreenPinIcon
        case "bright-pink": return BrightPinkPinIcon
        case "bright-yellow": return BrightYellowPinIcon
        default: return BrownPinIcon
    }
}

interface RegistAnswerModalProps {
    question_title: string
    question_id: string
    isShowing: boolean
    onAddComment: (comment: IceBreakingCommentDto) => void
    onShowModal: (value: boolean) => void
}

type MemoColor = "yellow" | "purple" | "pink" | "sky-blue"
type PinColor = "brown" | "bright-green" | "bright-yellow" | "bright-pink"