"use client"

import Image from "next/image"
import { Suspense, useEffect, useState } from "react"

import LoadingIndicator from "@/app/(common)/(component)/(loading)"

import EmojiIcon from "../../../../../../../public/image/emoji1.png"
import UnSubscribeIcon from "../../../../../../../public/image/un_subscribe.png"
import SubscribeIcon from "../../../../../../../public/image/subscribe.png"
import styles from "./question.module.css"

function Question() {
    return (
        <Suspense fallback={<Loading/>}>
            <Loaded/>
        </Suspense>
    )
}

function Loading() {
    return (
        <div className={styles.container} style={{ justifyContent: "center" }}>
            <LoadingIndicator spinnerCount={2}/>
        </div>
    )
}

async function Loaded() {
    const { height } = useWindowSize()
    const emojiSize = ((height ?? 1080) / 1080) * 100

    return (
        <div className={styles.container} style={{ justifyContent: "space-between" }}>
            <span>오늘의 질문!</span>
            <Image
            src={UnSubscribeIcon}
            alt="구독 아이콘"
            className={styles.button}
            />
            <Image
            src={EmojiIcon}
            alt="이모지 아이콘"
            width={emojiSize}
            height={emojiSize}
            />
            <p>요즘 내가 갖고 있는 가장 큰 불만거리는 무엇인가요?</p>
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