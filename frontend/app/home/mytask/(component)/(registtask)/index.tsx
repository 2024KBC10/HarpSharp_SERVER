"use client"

import Image from "next/image"
import { useState } from "react"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"
import RegistTaskInputField from "./regist_task_input_field"

import CloseIcon from "../../../../../public/image/close.png"
import styles from "./regist_task.module.css"

export default function RegistTask({
    isOpen,
    onCloseRegistTaskWindow,
}: RegistTaskProps) {
    const [title, setTitle] = useState<string>("")
    const [memo, setMemo] = useState<string>("")
    const [start, setStart] = useState<string>("")
    const [end, setEnd] = useState<string>("")

    return (
        <div 
        className={styles.container}
        style={{  width: isOpen ? "30%" : "0px" }}
        >
            <div 
            className={styles.top_container}
            style={{  
                width: isOpen ? "100%" : "0px", 
                opacity: isOpen ? 1 : 0,
                display: isOpen? "flex" : "none",
            }}
            >
                <Image
                src={CloseIcon}
                alt="닫기 아이콘"
                width={16}
                height={16}
                className={styles.close}
                onClick={onCloseRegistTaskWindow}
                />
                <span>일정 만들기</span>
            </div>
            <div 
            className={styles.body_container}
            style={{  
                width: isOpen ? "100%" : "0px", 
                opacity: isOpen ? 1 : 0,
                display: isOpen? "flex" : "none",
            }}
            >
                <div className={styles.input_container}>
                    <RegistTaskInputField
                    category="일정 이름"
                    placeholder="일정 이름을 입력해주세요."
                    onChange={setTitle}
                    isRequired={true}
                    />
                    <RegistTaskInputField
                    category="메모"
                    placeholder="메모를 입력해주세요."
                    onChange={setMemo}
                    isRequired={true}
                    />
                    <div className={styles.date_input_container}>
                        <RegistTaskInputField
                        category="시작일"
                        placeholder="ex) 0000-00-00"
                        onChange={setStart}
                        isRequired={true}
                        maxLength={10}
                        />
                        <Spacer spacing={10}/>
                        <p>~</p>
                        <Spacer spacing={10}/>
                        <RegistTaskInputField
                        category="종료일"
                        placeholder="ex) 0000-00-00"
                        onChange={setEnd}
                        isRequired={true}
                        maxLength={10}
                        />
                    </div>
                </div>
                <div className={styles.button_wrapper}>
                    <TextButton
                    text="일정 등록"
                    type="blue"
                    onClick={() => {}}
                    borderRadius={5}
                    />
                </div>
            </div>
        </div>
    )
}

interface RegistTaskProps {
    isOpen: boolean
    onCloseRegistTaskWindow: () => void
}