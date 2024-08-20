"use client"

import Spacer from "@/app/(common)/(component)/(spacer)"
import { useRouter } from "next/navigation"

import styles from "./my_tasks.module.css"

export default function MyTasks() {
    const router = useRouter()
    const onClick = () => router.push("/home/mytask")
    return (
        <div className={styles.container}>
            <div className={styles.title_wrapper}>
                <span>내 일정</span>
            </div>
            <div className={styles.mytasks_wrapper}>
                <span>나만의 일정을 만들어 관리해 보세요!</span>
                <Spacer spacing={10} direction="column"/>
                <div className={styles.button} onClick={onClick}>
                    <span>일정 만들기</span>
                </div>
            </div>
        </div>
    )
}