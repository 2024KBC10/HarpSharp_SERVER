import Image from "next/image"

import AIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import UserIcon from "../../../../public/image/default_user_icon.png"
import styles from "./ai_chat_bubble.module.css"
import Spacer from "../(spacer)"

export default function ChatBubble({
    content,
    role,
}: ChatBubbleProps) {
    content = "<p>" + content.replaceAll(/\s\s/g, "<br/>") + "</p>"
    return role === "user" ? UserChatBubble(content) : AIChatBubble(content)
}

function UserChatBubble(content: string) {
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

function AIChatBubble(content: string) {
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
                <div 
                className={styles.chat_bubble_wrapper}
                style={{ backgroundColor: "#dfe5ff" }}
                dangerouslySetInnerHTML={{ __html: content }}
                >
                </div>
            </div>
        </li>
    )
}

interface ChatBubbleProps {
    content: string
    role: "user" | "ai"
}