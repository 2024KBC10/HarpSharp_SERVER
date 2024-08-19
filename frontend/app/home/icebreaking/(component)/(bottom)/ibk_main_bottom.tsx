"use client"
import Question from "./(component)/(question)"
import styles from "./ibk_main_bottom.module.css"
import {APIManager} from "@/app/(common)/(api)"

export default function IBKMainBottom() {
    const onClick = async () => {
        try {
            const result = await APIManager.post({
                route: "api/v1/join",
                body: {
                    password: "1234",
                    username: "asd",
                    email: "ryoo0504@gmail.com"
                }
            });
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