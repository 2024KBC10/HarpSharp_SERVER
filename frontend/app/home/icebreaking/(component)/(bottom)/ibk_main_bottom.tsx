"use client"
import Question from "./(component)/(question)"
import styles from "./ibk_main_bottom.module.css"
import {APIManager} from "@/app/(common)/(api)"

export default function IBKMainBottom() {
    const onClick = async () => {
        try {
            const result = await APIManager.post({
                route: "/gpt/chat",
                body: {
                    prompt: "---Context: \"너의 소속은 harpsharp야\"---너 소속이 어디야?"
                }
            })
            console.log(result);

        } catch(e) { throw e }
    }
    return (
        <div className={styles.container}>
            <span onClick={onClick}>다른 질문 들</span>
            <ul className={styles.question_container}>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
            </ul>
        </div>
    )
}