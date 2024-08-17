"use client"

import { useRouter } from "next/navigation"

import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./question.module.css"

export default function Question() {
    const router = useRouter()
    const onClick = () => router.push("/home/icebreaking/1")
    return (
        <li className={styles.container} onClick={onClick}>
            <p>질문 내용</p>
            <div className={styles.action_container}>
                <p>0개의 관심</p>
                <Spacer spacing={10} direction="row"/>
                <p>0개의 답변</p>
            </div>
        </li>
    )
}