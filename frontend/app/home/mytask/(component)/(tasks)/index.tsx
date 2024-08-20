import Spacer from "@/app/(common)/(component)/(spacer)"
import Task from "../(task)"

import styles from "./tasks.module.css"

export default function Tasks({
    type,
    onChangeShowPage,
}: TasksProps) {
    const state = type === "progress" ? StateProgress() : type === "stop" ? StateStop() : StateSuccess()
    return (
        <div className={styles.container}>
            <div className={styles.state_container}>
                <Spacer spacing={10} direction="row"/>
                {state}
                <Spacer spacing={10} direction="row"/>
                <span>0</span>
            </div>
            <ul className={styles.task_wrapper}>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
                <Task onChangeShowPage={onChangeShowPage}/>
            </ul>
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
    type: "progress" | "stop" | "success"
    onChangeShowPage: () => void
}