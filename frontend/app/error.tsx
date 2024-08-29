"use client"

import { useRouter } from "next/navigation"

import TextButton from "./(common)/(component)/(button)"
import Spacer from "./(common)/(component)/(spacer)"

import styles from "./error.module.css"

export default function ErrorPage({
    error,
    reset,
}: {
    error: Error & { digest?: string }
    reset: () => void
}) {
    const router = useRouter()
    return (
        <div className={styles.container}>
            <span>OOPS!</span>
            <Spacer spacing={10} direction="column"/>
            <p>다음과 같은 이유로 페이지 요청에 실패했어요:(</p>
            <br></br>
            <p dangerouslySetInnerHTML={{ __html: error.message }} style={{ fontWeight: "bold", fontSize: "1em" }}></p>
            <Spacer spacing={20} direction="column"/>
            <TextButton
            text="홈"
            type="blue"
            onClick={() => router.push("/home")}
            />
        </div>
    )
}