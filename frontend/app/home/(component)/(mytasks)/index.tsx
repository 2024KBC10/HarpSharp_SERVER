"use client"

import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"

import { TaskDto, TaskState } from "@/app/(common)/(interface)"
import { userModel } from "@/app/(common)/(model)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./my_tasks.module.css"

export default function MyTasks() {
    const [myTasks, setMyTasks] = useState<TaskDto[]>([])

    const router = useRouter()
    const onClick = () => router.push("/home/mytask")

    useEffect(() => {
        const user = userModel.getUserData()
        if(user) setMyTasks(user.myTodos)
    }, [])

    return (
        <div className={styles.container}>
            <div className={styles.title_wrapper}>
                <span>내 일정</span>
            </div>
            <div className={styles.mytasks_wrapper}>
                {
                    myTasks.length > 0
                    ?  <ul>
                        {
                            myTasks.map((task, index) => (
                                <TaskStatus key={index} title={task.title} task_status={task.status}/>
                            ))
                        }
                       </ul>
                    : <>
                        <span>나만의 일정을 만들어 관리해 보세요!</span>
                        <Spacer spacing={10} direction="column"/>
                        <div className={styles.button} onClick={onClick}>
                            <span>일정 만들기</span>
                        </div>
                      </>
                }
            </div>
        </div>
    )
}

function TaskStatus({ title, task_status }: { title: string, task_status: TaskState }) {
    let status = "진행중"
    let color = "#00C8FF"

    switch(task_status) {
        case "RUNNING": break
        case "STOP":
            status = "중지"
            color = "#DA342E"
            break
        case "DONE":
            status = "완료"
            color = "#24E34D"
            break
    }
    return (
        <li>
            <span>{title}</span>
            <div className={styles.state_container}>
                <span>{status}</span>
                <Spacer spacing={5} direction="row"/>
                <div 
                className={styles.state}
                style={{
                    backgroundColor: color,
                    boxShadow: `0px 0px 5px 1px ${color}`
                }}
                />
            </div>
        </li>
    )
}