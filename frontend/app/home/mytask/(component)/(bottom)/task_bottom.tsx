"use client"

import Image from "next/image"
import { useState } from "react"

import TextButton from "@/app/(common)/(component)/(button)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import DropdownArrow from "../../../../../public/image/arrow_down.png"
import DeleteIcon from "../../../../../public/image/delete.png"
import styles from "./task_bottom.module.css"

export default function MyTaskBottom() {
    return (
        <div className={styles.container}>
            <span>작업</span>
            <ul className={styles.task_container}>
                {
                    [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
                    .map(v => TaskItem({ name: `${v}`, key: v })) 
                }
            </ul>
            <div className={styles.input_container}>
                <input
                placeholder="추가할 작업의 이름을 입력해 주세요."
                />
                <TextButton
                text="추가"
                type="blue"
                onClick={() => {}}
                />
            </div>
        </div>
    )
}

function TaskItem({
    name,
    key,
}: TaskItemProps) {
    const [isOpen, setIsOpen] = useState<boolean>(false)
    return (
        <li 
        key={key} 
        className={styles.task}
        style={{ height: isOpen ? "100px" : undefined }}
        >
            <div className={styles.task_wrapper}>
                <span>{name}</span>
                <div className={styles.task_action_container}>
                    <p>미완료</p>
                    <Spacer spacing={5} direction="row"/>
                    <Image
                    src={DeleteIcon}
                    alt="삭제 아이콘"
                    className={styles.delete}
                    />
                    <Image
                    src={DropdownArrow}
                    alt="화살표 아이콘"
                    className={styles[`arrow_${isOpen ? "up" : "down"}`]}
                    onClick={() => setIsOpen(!isOpen)}
                    />
                </div>
            </div>
            <div className={styles.task_memo_container} style={{ display: isOpen ? "inline-block" : "none" }}>
            contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent
            </div>
            <input id={`clear_${key}`} type="checkbox"/>
            <label style={{ display: isOpen ? "inline-block" : "none" }} htmlFor={`clear_${key}`}/>
        </li>
    )
}

interface TaskItemProps {
    name: string
    key: number
}

// 작업 메모 글자 제한 200자