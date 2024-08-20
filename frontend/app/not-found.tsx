"use client"

import { useRouter } from "next/navigation"

import TextButton from "./(common)/(component)/(button)"
import Spacer from "./(common)/(component)/(spacer)"

import styles from "./error.module.css"

export default function NotFoundPage() {
    const router = useRouter()
    return (
        <div className={styles.container}>
            <span>길을 잃으셨군요!</span>
            <Spacer spacing={10} direction="column"/>
            <p>아래 버튼을 눌러 홈 페이지로 이동 할 수 있어요!</p>
            <Spacer spacing={20} direction="column"/>
            <TextButton
            text="홈"
            type="blue"
            onClick={() => router.push("/home")}
            />
        </div>
    )
}