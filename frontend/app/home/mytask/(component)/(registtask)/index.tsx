"use client"

import Image from "next/image"
import { useState } from "react"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"
import InputField from "@/app/(common)/(component)/(inputfield)"
import { userModel } from "@/app/(common)/(model)"
import { APIManager } from "@/app/(common)/(api)"
import { LocalStorage } from "@/app/(common)/(storage)"
import { TaskDto, TaskJson } from "@/app/(common)/(interface)"
import { TaskProvider } from "@/app/(common)/(provider)"


import CloseIcon from "../../../../../public/image/close.png"
import styles from "./regist_task.module.css"

export default function RegistTask({
    isOpen,
    onAddTask,
    onCloseRegistTaskWindow,
}: RegistTaskProps) {
    const [title, setTitle] = useState<string>("")
    const [memo, setMemo] = useState<string>("")
    const [start, setStart] = useState<string>("")
    const [end, setEnd] = useState<string>("")

    const syncDateFromServer = (target: Date) => {
        const year = target.getFullYear()
        const month = `${target.getMonth() + 1}`.padStart(2, "0")
        const day = `${target.getDate()}`.padStart(2, "0")
        return `${year}-${month}-${day} 00:00:00`
    }

    const dateValidator = (str: string) => {
        const reg = /[0-9]{4}-[0-9]{2}-[0-9]{2}/
        return reg.test(str)
    }

    const registTask = async () => {
        try {
            const user = userModel.getUserData()

            if(user === null) alert("로그인이 필요한 서비스 입니다.")
            else if(!dateValidator(start) || !dateValidator(end)) {
                alert("시작일과 종료일을 양식에 맞게 작성 해주세요.")
                return
            } else if(title.length < 0 || memo.length < 0) {
                alert("제목과 메모는 최소 한 글자 이상 기입하여야 합니다.")
                return
            }
            const token = LocalStorage.get("token")
            const startAt = syncDateFromServer(new Date(start))
            const endAt = syncDateFromServer(new Date(end))

            const response = await APIManager.post<TaskJson>({
                route: "/todo/posts",
                body: {
                    username: user!.username,
                    title,
                    content: memo,
                    status: "RUNNING",
                    startAt,
                    endAt,
                },
                headers: { authorization: `${token}` }
            })
            
            if("data" in response && response.data) {
                const key = Object.keys(response.data)[0]
                const task = TaskProvider.toDto(key, response.data[key])
                setTitle("")
                setMemo("")
                setStart("")
                setEnd("")
                onAddTask(task)
                return
            }
            alert("일정 등록에 실패했습니다.")
        } catch(e) {
            console.log(e)
            alert("일정 등록에 실패했습니다.")
            return
        }
    }

    return (
        <div 
        className={styles.container}
        style={{ width: isOpen ? "30%" : "0px" }}
        >
            <div 
            className={styles.top_container}
            style={{  
                width: isOpen ? "100%" : "0px", 
                opacity: isOpen ? 1 : 0,
                display: isOpen ? "flex" : "none",
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
                    <InputField
                    category="일정 이름"
                    placeholder="일정 이름을 입력해주세요."
                    onChange={setTitle}
                    isRequired={true}
                    />
                    <InputField
                    category="메모"
                    placeholder="메모를 입력해주세요."
                    onChange={setMemo}
                    isRequired={true}
                    />
                    <div className={styles.date_input_container}>
                        <InputField
                        category="시작일"
                        placeholder="ex) 0000-00-00"
                        onChange={setStart}
                        isRequired={true}
                        maxLength={10}
                        />
                        <Spacer spacing={10}/>
                        <p>~</p>
                        <Spacer spacing={10}/>
                        <InputField
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
                    onClick={registTask}
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
    onAddTask: (task: TaskDto) => void
}