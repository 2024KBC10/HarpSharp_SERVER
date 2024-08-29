"use client"

import Image from "next/image"
import { 
    useEffect, 
    useRef, 
    useState 
} from "react"

import { APIManager } from "../../(api)"
import ChatBubble from "./ai_chat_bubble"
import Spacer from "../(spacer)"

import AIIcon from "../../../../public/image/AI_Icon_entered_phone.png"
import SendIcon from "../../../../public/image/send.png"
import styles from "./ai_phone.module.css"

export default function AIPhone({
    isOpen
}: AIPhoneProps) {
    const [input, setInput] = useState<string>("")
    const [bubbles, setBubbles] = useState<IChatBubble[]>([])
    const [isAutoScroll, setIsAutoScroll] = useState(true)
    const chatContainerRef = useRef<HTMLUListElement>(null)

    const scrollToBottom = () => {
        if (chatContainerRef.current && isAutoScroll) {
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
        }
    }

    const handleScroll = () => {
        if (chatContainerRef.current) {
            const { scrollTop, scrollHeight, clientHeight } = chatContainerRef.current
            const isAtBottom = scrollTop + clientHeight >= scrollHeight - 10
            setIsAutoScroll(isAtBottom)
        }
    }
    
    const sendQuestion = async () => {
        try {
            if(input.length > 0) {
                const prompt = input
                const question_bubble: IChatBubble = { message: prompt, role: "user" }
                setBubbles([...bubbles, question_bubble])
                setInput("")

                await APIManager.post<{ generated_text: string }>({
                    route: "/gpt/chat",
                    body: { prompt }
                })
                .
                then(result => {
                    if("data" in result) {
                        const answer_bubble: IChatBubble = { message: result['data']!['generated_text'], role: "ai" }
                        setBubbles([...bubbles, question_bubble, answer_bubble])
                    }
                })
            }
        } catch(e) {
            alert("서버로 부터 응답을 받지 못했습니다.")
            return
        }

    }

    useEffect(() => { scrollToBottom() }, [bubbles])

    return (
        <div className={styles[`container_${isOpen ? "open" : "close"}`]}>
            <div className={styles.title_container}>
                <Image
                src={AIIcon}
                width={48}
                height={48}
                alt="AI"
                />
                <Spacer spacing={5}/>
                <span>AI_Shark</span>
            </div>
            <ul 
            className={styles.chat_container}
            ref={chatContainerRef}
            onScroll={handleScroll}
            >
                <li className={styles.channel_info_container}>
                    <Image
                    src={AIIcon}
                    width={64}
                    height={64}
                    alt="AI"
                    />
                    <Spacer spacing={10} direction="column"/>
                    <span>HarpSharp 챗봇</span>
                    <Spacer spacing={2.5} direction="column"/>
                    <p>AI에게 무엇이든 질문해 보세요!</p>
                </li>
                <ChatBubble
                content="안녕하세요! AI_Shark 입니다.
                무엇이든 물어보세요!"
                role="ai"
                />
                { 
                    bubbles.map((bubble, index) => (
                        <ChatBubble 
                        key={index} 
                        content={bubble.message} 
                        role={bubble.role}/>
                    )) 
                }
                <Spacer spacing={100} direction="column"/>
            </ul>
            <div className={styles.input_container}>
                <textarea
                rows={1}
                value={input}
                spellCheck={false}
                wrap="virtual"
                onChange={(e) => setInput(e.target.value)}
                placeholder="메시지를 입력해 주세요."
                />
                <div onClick={sendQuestion} className={styles.send}><Image src={SendIcon} alt="전송 아이콘"/></div>
            </div>
        </div>
    )
}

interface AIPhoneProps {
    isOpen: boolean
}

interface IChatBubble {
    message: string
    role: "user" | "ai"
}