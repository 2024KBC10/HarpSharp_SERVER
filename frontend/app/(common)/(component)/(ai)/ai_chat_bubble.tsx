import Image from "next/image"
import { useState } from "react"

import AIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import UserIcon from "../../../../public/image/default_user_icon.png"
import styles from "./ai_chat_bubble.module.css"
import Spacer from "../(spacer)"

export default function ChatBubble({
    content,
    role,
    answer_type = "text",
}: ChatBubbleProps) {
    if(!/(https:\/\/)/.test(content)) content = "<p>" + content.replaceAll(/\s\s/g, "<br/>") + "</p>"
    return role === "user" ? UserChatBubble(content, answer_type) : AIChatBubble(content, answer_type)
}

function UserChatBubble(content: string, type: ChatBubbleType) {
    return (
        <li className={styles.user_chat_bubble_container}>
            <div 
            className={styles.chat_bubble_container}
            >
                <span style={{ 
                    alignSelf: "flex-end",
                    marginLeft: "0px",
                    marginRight: "10px", 
                }}>kangmin.han(한강민) / 클라우드</span>
                <Spacer spacing={5} direction="column"/>
                <div 
                className={styles.chat_bubble_wrapper}
                style={{ backgroundColor: "#DCF8C6" }}
                dangerouslySetInnerHTML={{ __html: content }}
                >
                </div>
            </div>
            <Image
            width={24}
            height={24}
            src={UserIcon}
            className={styles.icon}
            alt="AI Shark 아이콘"
            />
        </li>
    )
}

function AIChatBubble(content: string, type: ChatBubbleType) {
    const [isHover, setIsHover] = useState<boolean>()

    const onOpenImageWindow = () => {
        if(typeof window !== undefined) {
            window.open(content, "_blank", "noopener,noreferrer")
        }
    }
    return (
        <li className={styles.ai_chat_bubble_container}>
            <Image
            width={24}
            height={24}
            src={AIIcon}
            className={styles.icon}
            alt="AI Shark 아이콘"
            />
            <div 
            className={styles.chat_bubble_container}
            >
                <span>AI Shark</span>
                <Spacer spacing={5} direction="column"/>
                {
                    type === "text"
                    ? <div 
                      className={styles.chat_bubble_wrapper}
                      style={{ backgroundColor: "#dfe5ff" }}
                      dangerouslySetInnerHTML={{ __html: content }}
                      />
                    : <div 
                      className={styles.chat_bubble_image_wrapper}
                      onMouseOver={(_) => setIsHover(true)}
                      onMouseLeave={(_) => setIsHover(false)}
                      >
                        <div className={styles.hint} style={{ opacity: `${isHover ? 1 : 0}`, display: isHover ? "block" : "none" }}>이미지를 클릭하면 크게 볼 수 있습니다</div>
                        <Image src={content} onClick={onOpenImageWindow} width={200} height={200} alt="AI 생성 이미지"/>
                      </div>
                }
            </div>
        </li>
    )
}

interface ChatBubbleProps {
    content: string
    answer_type: ChatBubbleType
    role: "user" | "ai"
}

export type ChatBubbleType = "text" | "image"