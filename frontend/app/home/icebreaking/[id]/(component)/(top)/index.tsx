"use client"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"

import styles from "./ibk_detail_top.module.css"

export default function IBKDetailTop({
    onShowModal
}: IBKDetailTopProps) {
    return (
        <div className={styles.container}>
            <span>질문 이름</span>
            <Spacer spacing={10} direction="column"/>
            <div className={styles.info_container}>
                <div className={styles.action_container}>
                    <Spacer spacing={20}/>
                    <span>0개의 관심</span>
                    <Spacer spacing={5}/>
                    <span>0개의 답변</span>
                    <Spacer spacing={5}/>
                    <TextButton
                    text="답변 등록"
                    type="blue"
                    onClick={() => onShowModal(true)}
                    fontSize={12}
                    width={65}
                    height={10}
                    />
                </div>
                <span>등록일 2024.07.20</span>
            </div>
        </div>
    )
}

interface IBKDetailTopProps {
    onShowModal: (value: boolean) => void
}