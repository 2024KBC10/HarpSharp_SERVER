"use client"

import Image from "next/image"
import { 
    useCallback,
    useEffect, 
    useRef, 
    useState 
} from "react"

import { TaskDto, TaskState } from "../../../../(common)/(interface)/task.dto"
import { APIManager } from "@/app/(common)/(api)"
import { userModel } from "@/app/(common)/(model)"
import { LocalStorage } from "@/app/(common)/(storage)"
import Tasks from "../(tasks)"
import TextButton from "@/app/(common)/(component)/(button)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import CloseIcon from "../../../../../public/image/close.png"
import styles from "./main_bottom.module.css"

export default function MyTaskMainBottom({
    tasks,
    onRemoveTask,
    onRefreshTasks,
}: MyTaskMainBottom) {
    const [showModal, setShowModal] = useState<boolean>(false)
    const [runningTasks, setRunningTasks] = useState<TaskDto[]>([])
    const [stopTasks, setStopTasks] = useState<TaskDto[]>([])
    const [doneTasks, setDoneTasks] = useState<TaskDto[]>([])

    const curr_task = useRef<TaskDto>()

    const onOpenModal = (task: TaskDto) => {
        curr_task.current = task
        setShowModal(true)
    }
    const onCloseModal = () => setShowModal(false)
    const sortTasks = useCallback(() => {
        const running: TaskDto[] = []
        const stop: TaskDto[] = []
        const done: TaskDto[] = []

        for(let i=0; i<tasks.length; ++i) {
            const task = tasks[i]
            switch(task.status) {
                case "RUNNING":
                    running.push(task)
                    break
                case "STOP":
                    stop.push(task)
                    break
                case "DONE":
                    done.push(task)
                    break
            }
        }

        setRunningTasks(running)
        setStopTasks(stop)
        setDoneTasks(done)
    }, [tasks])

    useEffect(() => { sortTasks() }, [sortTasks])

    function EditTaskStateModal() {
        const syncDateFromServer = (target: Date) => {
            const year = target.getFullYear()
            const month = `${target.getMonth() + 1}`.padStart(2, "0")
            const day = `${target.getDate()}`.padStart(2, "0")
            return `${year}-${month}-${day} 00:00:00`
        }
        const isValidState = (state: string) => {
            switch(state) {
                case "RUNNING":
                case "STOP":
                case "DONE": return true
                default: return false
            }
        }
        const onChangeTaskState = async (state: string) => {
            try {
                const user = userModel.getUserData()
                if(!curr_task.current || curr_task.current.status === state || !isValidState(state)) return
                else if(!user) {
                    alert("로그인이 필요한 서비스 입니다.")
                    return
                }
                
                const task = curr_task.current
                const token = LocalStorage.get("token")
                const result = await APIManager.patch({
                    route: "/todo/posts",
                    body: {
                        "postId": task.id,
                        "username": user.username,
                        "title": task.title,
                        "content": task.content,
                        "status": state,
                        "startAt": syncDateFromServer(new Date(task.startAt)),
                        "endAt": syncDateFromServer(new Date(task.endAt))
                    },
                    headers: { authorization: `${token}` }
                })

                if("data" in result) {
                    task.status = state as TaskState
                    onCloseModal()
                    onRefreshTasks(task)
                    return
                }
                throw new Error(`API 요청 실패: ${result.code}`)
            } catch(e) {
                console.log(e)
                alert("일정 상태 변경에 실패했습니다.")
                return
            }
        }
        const onDeleteTask = async () => {
            try {
                const user = userModel.getUserData()
                if(!curr_task.current) return
                else if(!user) {
                    alert("로그인이 필요한 서비스 입니다.")
                    return
                }

                const task = curr_task.current
                const token = LocalStorage.get("token")
                const result = await APIManager.del({
                    route: "/todo/posts",
                    body: {
                        "postId": task.id,
                        "username": user.username,
                        "title": task.title,
                        "content": task.content,
                        "status": task.status,
                        "startAt": syncDateFromServer(new Date(task.startAt)),
                        "endAt": syncDateFromServer(new Date(task.endAt))
                    },
                    headers: { authorization: `${token}` }
                })

                if(typeof result === "boolean" && result) {
                    onCloseModal()
                    onRemoveTask(task)
                    return
                }
                throw new Error(`API 요청 실패`)
            } catch(e) {
                console.log(e)
                alert("일정 상태 변경에 실패했습니다.")
                return
            }
        }

        return (
            <div className={styles.state_modal_background}>
                <div className={styles.state_modal}>
                    <div 
                    className={styles.row_container}
                    style={{ justifyContent: "space-between" }}
                    >
                        <span>{curr_task.current?.title}</span>
                        <Image
                        width={12}
                        height={12}
                        src={CloseIcon}
                        style={{ cursor: "pointer" }}
                        alt="닫기 아이콘"
                        onClick={onCloseModal}
                        />
                    </div>
                    <p>선택하신 일정의 상태를 변경 하시겠어요?<br/><br/>진행중으로 되돌릴 수 없어요!</p>
                    <div className={styles.row_container}>
                        <TextButton
                        text="완료"
                        onClick={() => onChangeTaskState("DONE")}
                        width={65}
                        height={15}
                        fontSize={14}
                        />
                        <Spacer spacing={5} direction="row"/>
                        <TextButton
                        text="중단"
                        type="blue"
                        onClick={() => onChangeTaskState("STOP")}
                        width={65}
                        height={15}
                        fontSize={14}
                        />
                        <Spacer spacing={5} direction="row"/>
                        <TextButton
                        text="삭제"
                        type="blue"
                        onClick={onDeleteTask}
                        width={65}
                        height={15}
                        fontSize={14}
                        />
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className={styles.container}>
            <Tasks tasks={runningTasks} onOpenModal={onOpenModal} type="RUNNING"/>
            <Tasks tasks={stopTasks} onOpenModal={onOpenModal} type="STOP"/>
            <Tasks tasks={doneTasks} onOpenModal={onOpenModal} type="DONE"/>
            {
                showModal
                ? <EditTaskStateModal/>
                : <></>
            }
        </div>
    )
}

interface MyTaskMainBottom {
    tasks: TaskDto[]
    onRemoveTask: (task: TaskDto) => void
    onRefreshTasks: (task: TaskDto) => void
}