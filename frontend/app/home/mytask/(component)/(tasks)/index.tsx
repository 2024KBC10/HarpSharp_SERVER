import { TaskDto, TaskState } from "../../../../(common)/(interface)/task.dto"
import Spacer from "@/app/(common)/(component)/(spacer)"
import Task from "../(task)"

import styles from "./tasks.module.css"

export default function Tasks({
    type,
    tasks,
    onOpenModal,
}: TasksProps) {
    const state = type === "RUNNING" ? StateProgress() : type === "STOP" ? StateStop() : StateSuccess()
    return (
        <div className={styles.container}>
            <div className={styles.state_container}>
                <Spacer spacing={10} direction="row"/>
                {state}
                <Spacer spacing={10} direction="row"/>
                <span>{tasks.length}</span>
            </div>
            {
                tasks.length > 0
                ? <ul className={styles.task_wrapper}>
                    {tasks.map((task, index) => (<Task task={task} key={index} onOpenModal={onOpenModal}/>))}
                  </ul>
                : <div className={styles.task_wrapper} style={{ overflow: "auto", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "bold" }}><span>등록 된 일정이 없습니다</span></div>
            }
        </div>
    )
}

function StateProgress() {
    return (<div className={styles.state_progress}>진행중</div>)
}
function StateStop() {
    return (<div className={styles.state_stop}>중단</div>)
}
function StateSuccess() {
    return (<div className={styles.state_success}>완료</div>)
}

interface TasksProps {
    type: TaskState
    tasks: TaskDto[]
    onOpenModal: (task: TaskDto) => void
}