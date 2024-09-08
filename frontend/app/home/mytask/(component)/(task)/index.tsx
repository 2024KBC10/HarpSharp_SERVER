import Image from "next/image"
import { useEffect, useState } from "react"

import Spacer from "@/app/(common)/(component)/(spacer)"
import { TaskDto } from "../../../../(common)/(interface)/task.dto"
import { userModel } from "@/app/(common)/(model)"

import FocusIcon from "../../../../../public/image/accuracy.png"
import CalendarIcon from "../../../../../public/image/calendar.png"
import FlagIcon from "../../../../../public/image/flag.png"
import styles from "./task.module.css"

export default function Task({
    task,
    onOpenModal,
}: TaskProps) {
    const [isFocusTask, setIsFocusTask] = useState<boolean>(false)

    const startAt = new Intl.DateTimeFormat("ko", { dateStyle: "long" }).format(new Date(task.startAt))
    const endAt = new Intl.DateTimeFormat("ko", { dateStyle: "long" }).format(new Date(task.endAt))

    useEffect(() => {
        const focusTask = userModel.getFocusTask()
        if(focusTask && focusTask.id === task.id) {
            setIsFocusTask(true)
        }
    },[task.id])

    const openModal = () => {
        if(isFocusTask) {
            alert("집중공략중인 일정은 수정이 불가합니다.")
            return
        }
        onOpenModal(task)
    }

    return (
        <li 
        className={styles.container}
        onClick={openModal}
        >
            <div className={styles.title_wrapper}>
                <span>{task.title}</span>
                {
                    isFocusTask
                    ? <Image
                      src={FocusIcon}
                      width={18}
                      height={18}
                      alt="집중공략 아이콘"
                      />
                    : <></>
                }
            </div>
            <div className={styles.info_container}>
                <div className={styles.data_container}>
                    <Image
                    src={CalendarIcon}
                    alt="달력 아이콘"
                    />
                    <Spacer spacing={5} direction="row"/>
                    <div className={styles.data_wrapper}>
                        <p>시작 일</p>
                        <p>{startAt}</p>
                    </div>
                </div>
                <div className={styles.data_container}>
                    <Image
                    src={FlagIcon}
                    alt="깃발 아이콘"
                    />
                    <Spacer spacing={5} direction="row"/>
                    <div className={styles.data_wrapper}>
                        <p>목표 달성 기한</p>
                        <p>{endAt}</p>
                    </div>
                </div>
            </div>
        </li>
    )
}

interface TaskProps {
    task: TaskDto
    onOpenModal: (task: TaskDto) => void
}