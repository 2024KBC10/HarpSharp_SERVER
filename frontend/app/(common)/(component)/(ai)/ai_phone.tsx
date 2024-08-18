"use client"

import Image from "next/image"
import { useState } from "react"

import Spacer from "../(spacer)"

import EnteredAIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import SendIcon from "../../../../public/image/send.png"
import ChatBubbleIcon from "../../../../public/image/chat_bubble.png"
import styles from "./ai_phone.module.css"
import {APIManager} from "@/app/(common)/(api)";

export default function AIPhone({
                                    isOpen
                                }: AIPhoneProps) {
    const [input, setInput] = useState<string>("")
    const [messages, setMessages] = useState<Array<{role: string, content: string}>>([]); // 메시지 상태 관리

    const onClick = async () => {
        console.log(input);
        try {
            setMessages(prevMessages => [...prevMessages, { role: 'user', content: input }]);
            const result = await APIManager.post({
                route: "gpt/chat",
                body: {
                    prompt: input
                }
            });

            if ("generated_text" in result) {
                const aiResponse = result.generated_text as string;
                setMessages(prevMessages => [...prevMessages, { role: 'ai', content: aiResponse }]);
            } else {
                console.error("Request failed:", result);
                // 실패 처리 로직 추가 가능
            }

            console.log(result);

        } catch(e) { throw e }
    }
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
                {messages.map((message, index) => (
                    <li key={index} className={message.role === 'user' ? styles.user_message : styles.ai_message}>
                        {message.content}
                    </li>
                ))}

            </ul>
            <div className={styles.input_container}>
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="메시지를 입력해 주세요."
                />
                <div className={styles.send} onClick={onClick}><Image src={SendIcon} alt="전송 아이콘"/></div>
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