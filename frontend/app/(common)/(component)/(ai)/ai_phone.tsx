"use client"

import Image from "next/image"
import React, { useState, useEffect, useRef } from "react"

import Spacer from "../(spacer)"

import EnteredAIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import SendIcon from "../../../../public/image/send.png"
import ChatBubbleIcon from "../../../../public/image/chat_bubble.png"
import styles from "./ai_phone.module.css"
import { APIManager } from "@/app/(common)/(api)";

export default function AIPhone({ isOpen }: AIPhoneProps) {
    const [input, setInput] = useState<string>("")
    const [messages, setMessages] = useState<Array<{ role: string, content: string }>>([]);
    const chatContainerRef = useRef<HTMLUListElement>(null); // Ref for the chat container
    const [isAutoScroll, setIsAutoScroll] = useState(true); // 자동 스크롤 여부 상태

    const scrollToBottom = () => {
        if (chatContainerRef.current && isAutoScroll) {
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
        }
    }

    useEffect(() => {
        scrollToBottom(); // 메시지가 추가될 때마다 스크롤을 맨 아래로 이동
    }, [messages]);

    const handleScroll = () => {
        if (chatContainerRef.current) {
            const { scrollTop, scrollHeight, clientHeight } = chatContainerRef.current;
            const isAtBottom = scrollTop + clientHeight >= scrollHeight - 10;
            setIsAutoScroll(isAtBottom); // 스크롤이 맨 아래에 있을 때만 자동 스크롤 활성화
        }
    }

    const onClick = async () => {
        if (input.trim() === "") return;

        setMessages(prevMessages => [...prevMessages, { role: 'user', content: input }]);
        setInput("");
        try {
            const result = await APIManager.post({
                route: "gpt/chat",
                body: {
                    prompt: input
                }
            });

            console.log(result);


            if ('data' in result) {
                const aiResponse = (result.data as { generated_text: string }).generated_text as string;
                setMessages(prevMessages => [...prevMessages, { role: 'ai', content: aiResponse }]);
            } else {
                console.error("Request failed:", result);
            }


        } catch (e) {
            console.error("Error during API call:", e);
        }
    }

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter") {
            onClick();
        }
    }

    return (
        <div className={styles[`container_${isOpen ? "open" : "close"}`]}>
            <div className={styles.title_container}>
                <Image
                    src={EnteredAIIcon}
                    alt="AI"
                />
                <Spacer spacing={5} />
                <span>AI_Shark</span>
            </div>
            <ul className={styles.chat_container} ref={chatContainerRef} onScroll={handleScroll}>
                {messages.map((message, index) => (
                    <li key={index} className={`${styles.message} ${message.role === 'user' ? styles.message_user : styles.message_ai}`}>
                        {message.content}
                    </li>
                ))}
            </ul>
            <div className={styles.input_container}>
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="메시지를 입력해 주세요."
                />
                <div className={styles.send} onClick={onClick}><Image src={SendIcon} alt="전송 아이콘" /></div>
            </div>
            <div className={styles.tab_container}>
                <Image
                    src={ChatBubbleIcon}
                    alt="탭 아이콘"
                />
                <Spacer spacing={2.5} direction="column" />
                <span>대화</span>
            </div>
        </div>
    )
}

interface AIPhoneProps {
    isOpen: boolean
}
