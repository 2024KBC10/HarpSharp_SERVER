import Link from "next/link"

import Spacer from "@/app/(common)/(component)/(spacer)"
import { IceBreakingDto } from "../../../../../../(common)/(interface)/icebreaking.dto"

import styles from "./question.module.css"

export default function Question({
    question,
}: QuestionProps) {
    return (
        <Link 
        href={{
            pathname: `/home/icebreaking/${question.id}`,
            query: {
                content: question.content,
                createdAt: `${question.createdAt}`,
            }
        }} 
        legacyBehavior
        >
            <li className={styles.container}>
                <p>{question.title}</p>
                <div className={styles.action_container}>
                    <p>0개의 관심</p>
                    <Spacer spacing={10} direction="row"/>
                    <p>{question.replies.length}개의 답변</p>
                </div>
            </li>
        </Link>
    )
}

interface QuestionProps {
    question: IceBreakingDto
}