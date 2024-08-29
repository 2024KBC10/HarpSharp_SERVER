"use client"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"

import styles from "./ibk_detail_top.module.css"

export default function IBKDetailTop({
    content,
    createdAt,
    onShowModal
}: IBKDetailTopProps) {
    
    return (
        <div className={styles.container}>
            <span>{content}</span>
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
                <span>등록일 {createdAt.toLocaleDateString()}</span>
            </div>
        </div>
    )
}

interface IBKDetailTopProps {
    content: string
    createdAt: Date
    onShowModal: (value: boolean) => void
}

// 데이터 서큘 로딩 인디케이터 추가 하기