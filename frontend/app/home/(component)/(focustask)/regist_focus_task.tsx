import { useEffect, useState } from "react";
import Image from "next/image";

import Spacer from "@/app/(common)/(component)/(spacer)";
import { TaskDto, TaskState } from "@/app/(common)/(interface)";
import { userModel } from "@/app/(common)/(model)";

import styles from "./regist_focus_task.module.css"
import CloseIcon from "../../../../public/image/close.png"
import TextButton from "@/app/(common)/(component)/(button)";

export default function RegistFocusTask({
    tasks,
    onSelectFocusTask,
    onCloseRegistModal,
}: RegistFocusTaskProps) {
    const [selTaskNum, setSelTaskNum] = useState<number>(-1)
    const [validTasks, setValidTasks] = useState<TaskDto[]>([])

    const getStateText = (status: TaskState) => {
        switch(status) {
            case "DONE": return "완료"
            case "RUNNING": return "진행중"
            case "STOP": return "중지"
        }
    }
    const toKoLocaleText = (date: Date) => new Intl.DateTimeFormat("ko", { dateStyle: "long" }).format(date)
    const onSelectTask = (index: number) => setSelTaskNum(index)
    const confirm = () => {
        if(selTaskNum < 0) return

        const selTask = tasks[selTaskNum]
        if(userModel.setFocusTask(selTask)) onSelectFocusTask(selTask)
        else alert("이미 진행중인 공략이 있습니다.")
    }

    useEffect(() => {
        const temp: TaskDto[] = []
        for(let i=0; i<tasks.length; ++i) {
            if(tasks[i].status === "RUNNING") temp.push(tasks[i])
        }
        setValidTasks(temp)
    }, [tasks])

    return (
        <div className={styles.regist_background}>
            <div className={styles.regist_container}>
                <Image
                src={CloseIcon}
                onClick={onCloseRegistModal}
                width={18}
                height={18}
                className={styles.close_icon}
                alt="닫기 아이콘"
                />
                <div className={styles.regist_task_container}>
                    {
                        validTasks.length <= 0
                        ? <p className={styles.empty_text}>등록 된 일정이 없습니다.</p>
                        : <ul>
                        {
                            tasks.map((task, index) => {
                                if(task.status === "RUNNING")
                                    return (
                                        <MyTaskItem 
                                        title={task.title}
                                        currIndex={selTaskNum}
                                        onSelectTask={onSelectTask}
                                        index={index} 
                                        key={index}
                                        />
                                    )
                                return <></>
                            })
                        }
                        </ul>
                    }
                    <div className={styles.prev_box}>
                        <span>선택 된 일정</span>
                        <div className={styles.sel_task_container}>
                            {
                                selTaskNum >= 0
                                ? <>
                                    <Spacer spacing={5} direction="column"/>
                                    <span>{tasks[selTaskNum].title}</span>
                                    <Spacer spacing={10} direction="column"/>
                                    <p className={styles.state_text}>{getStateText(tasks[selTaskNum].status)}</p>
                                    <Spacer spacing={10} direction="column"/>
                                    <p className={styles.date_text}>시작 한 일정은 끝날 때 까지 변경할 수 없습니다.</p>
                                    <p className={styles.date_text}>시작일 {toKoLocaleText(new Date(tasks[selTaskNum].startAt))}</p>
                                    <p className={styles.date_text}>종료일 {toKoLocaleText(new Date(tasks[selTaskNum].endAt))}</p>
                                </>
                                : <p>내 일정 목록에서 집중적으로 공략하고<br/>싶은 일정을 선택해보세요!</p>
                            }
                        </div>
                    </div>
                </div>
                <Spacer spacing={10} direction="column"/>
                <TextButton
                text="공략시작!"
                type="blue"
                fontSize={14}
                width={90}
                height={15}
                onClick={confirm}
                />
            </div>
        </div>
    )
}

interface RegistFocusTaskProps {
    tasks: TaskDto[]
    onSelectFocusTask: (task: TaskDto) => void
    onCloseRegistModal: () => void
}

function MyTaskItem({ 
    title,
    currIndex,
    index,
    onSelectTask,
}: { 
    title: string 
    currIndex: number
    index: number
    onSelectTask: (index: number) => void 
}) {
    return (
        <li>
            <span>{title}</span>
            <input
            type="checkbox"
            checked={currIndex === index}
            onChange={_ => {onSelectTask(index)}}
            />
        </li>
    )
}