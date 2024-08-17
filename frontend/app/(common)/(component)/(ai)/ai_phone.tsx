"use client"

import Image from "next/image"
import { useState } from "react"

import Spacer from "../(spacer)"

import EnteredAIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import SendIcon from "../../../../public/image/send.png"
import ChatBubbleIcon from "../../../../public/image/chat_bubble.png"
import styles from "./ai_phone.module.css"

export default function AIPhone({
    isOpen
}: AIPhoneProps) {
    const [input, setInput] = useState<string>("")
    return (
        <div className={styles[`container_${isOpen ? "open" : "close"}`]}>
            <div className={styles.title_container}>
                <Image
                src={EnteredAIIcon}
                alt="AI"
                />
                <Spacer spacing={5}/>
                <span>AI_Shark</span>
            </div>
            <ul className={styles.chat_container}>

            </ul>
            <div className={styles.input_container}>
                <input
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="메시지를 입력해 주세요."
                />
                <div className={styles.send}><Image src={SendIcon} alt="전송 아이콘"/></div>
            </div>
            <div className={styles.tab_container}>
                <Image
                src={ChatBubbleIcon}
                alt="탭 아이콘"
                />
                <Spacer spacing={2.5} direction="column"/>
                <span>대화</span>
            </div>
        </div>
    )
}

interface AIPhoneProps {
    isOpen: boolean
}