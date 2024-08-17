"use client"

import Image from "next/image"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"

import Spacer from "@/app/(common)/(component)/(spacer)"
import Answer from "./(component)/(answer)"
import TextButton from "@/app/(common)/(component)/(button)"

import EmojiIcon from "../../../../../public/image/emoji1.png"
import UnSubscribeIcon from "../../../../../public/image/un_subscribe.png"
import SubscribeIcon from "../../../../../public/image/subscribe.png"
import styles from "./ibk_main_top.module.css"

export default function IBKMainTop() {
    const router = useRouter()
    const [subscribe, setSubscribe] = useState<boolean>(false)

    // 보류
    const { height } = useWindowSize()
    const emojiSize = ((height ?? 1080) / 1080) * 100

    return (
        <div className={styles.container}>
            <div className={styles.left_container} style={{ justifyContent: "space-between" }}>
                <span>오늘의 질문!</span>
                <Image
                src={subscribe ? SubscribeIcon : UnSubscribeIcon}
                alt="구독 아이콘"
                className={styles.button}
                onClick={() => setSubscribe(!subscribe)}
                />
                <Image
                src={EmojiIcon}
                alt="이모지 아이콘"
                width={72}
                height={72}
                />
                <p>요즘 내가 갖고 있는 가장 큰 불만거리는 무엇인가요?</p>
            </div>
            <div className={styles.right_container}>
                <div className={styles.action_container}>
                    <span>0개의 관심</span>
                    <Spacer spacing={5}/>
                    <span>0개의 답변</span>
                    <Spacer spacing={5}/>
                    <TextButton
                    text="더 보기"
                    onClick={() => router.push("/home/icebreaking/1")}
                    type="blue"
                    width={65}
                    height={10}
                    fontSize={12}
                    />
                </div>
                <div className={styles.answer_container}>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                    <Answer/>
                </div>
            </div>
        </div>
    )
}

function useWindowSize() {
    const [size, setSize] = useState<Partial<{ width: number, height: number }>>({
        width: undefined,
        height: undefined,
    })

    useEffect(() => {
        const handleWindowSize = () => {
            if(typeof window === undefined) return
            setSize({
                width: window.innerWidth,
                height: window.innerHeight
            })
        }

        window.addEventListener("resize", handleWindowSize)
        return () => window.removeEventListener("resize", handleWindowSize)
    }, [])
    
    return size
}