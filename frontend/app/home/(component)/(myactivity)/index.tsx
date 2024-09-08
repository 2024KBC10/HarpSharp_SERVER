"use client"

import { useEffect, useState } from "react"

import { IActivityLogs, userModel } from "@/app/(common)/(model)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./my_activity.module.css"

export default function MyActivity() {
    const [logs, setLogs] = useState<IActivityLogs[]>([])
    useEffect(() => { setLogs(userModel.getLogs()) }, [])

    return (
        <div className={styles.container}>
            <div className={styles.title_wrapper}>
                <span>내 소식</span>
            </div>
            <div 
            className={styles.activity_wrapper}
            style={{ justifyContent: `${logs.length > 0 ? "flex-start" : "center"}` }}
            >
                {
                    logs.length > 0
                    ? <ul>
                        {
                            logs.map((log, index) => (
                            <LogItem log={log} key={index}/>
                            ))
                        }
                      </ul>
                    : <span>아직 활동 기록이 없어요!</span>
                }
            </div>
        </div>
    )
}

function LogItem({ log }: { log: IActivityLogs }) {
    const now = Date.now()
    const diff = Math.floor((now - new Date(log.createdAt).getTime()) / 1000)

    const minute = Math.floor(diff / 60)
    const hour = Math.floor(minute / 60)
    const day = Math.floor(hour / 24)
    
    const ago = day > 0 ? `${day}일 전` : hour > 0 ? `${hour}시간 전` : `${minute}분 전`
    
    return (
        <li className={styles.activity_logs_container}>
            <div className={styles.pin}>
                <div className={styles.head}></div>
                <div className={styles.body}></div>
            </div>
            <Spacer spacing={5} direction="row"/>
            <div className={styles.message}>
                <p>{log.content}</p>
                <span>{ago}</span>
            </div>
        </li>
    )
}