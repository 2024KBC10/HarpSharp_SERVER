"use client"

import Image, { StaticImageData } from "next/image"
import { useState } from "react"

import Spacer from "../(spacer)"

import SubscribeIcon from "../../../../public/image/subscribe.png"
import UnSubscribeIcon from "../../../../public/image/un_subscribe.png"
import DefaultIcon from "../../../../public/image/pin_default.png"
import styles from "./answer_detail.module.css"

export default function AnswerDetail({
    username,
    content,
    pinIcon,
    memoColor,
    createdAt,
}: AnswerDetailProps) {
    const [subscribe, setSubscribe] = useState<boolean>(false)
    
    return (
        <div className={styles.container}>
            <div className={styles.memo_container}>
                <Image
                src={pinIcon ?? DefaultIcon}
                width={18}
                height={18}
                alt="핀 아이콘"
                />
                <div 
                className={styles.content_wrapper}
                style={{ backgroundColor: `${memoColor}` }}
                >{content}</div>
            </div>
            <div className={styles.info_container}>
                <div>
                    <span>작성자</span>
                    <Spacer spacing={5} direction="column"/>
                    <p>{username}</p>
                    <p>0개의 관심을 받았습니다.</p>
                </div>
                <p>작성일 {createdAt.toLocaleDateString()}</p>
            </div>
            <Image
            src={subscribe ? SubscribeIcon : UnSubscribeIcon}
            alt="좋아요 아이콘"
            onClick={() => setSubscribe(!subscribe)}
            style={{ 
                cursor: "pointer",
                padding: "10px" 
            }}
            />
        </div>
    )
}

interface AnswerDetailProps {
    username: string
    pinIcon: StaticImageData | null
    memoColor: string
    content: string
    createdAt: Date
}