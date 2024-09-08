"use client"

import Image from "next/image"
import { useEffect, useState } from "react"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"
import { userModel } from "@/app/(common)/(model)"
import { 
    TaskDto, 
    UserDto 
} from "@/app/(common)/(interface)"

import ViewInArIcon from "../../../../public/image/view_in_ar.png"
import RegistFocusTask from "./regist_focus_task"
import styles from "./focus_task.module.css"

export default function FocusTask() {
    const [openRegistModal, setOpenRegistModal] = useState<boolean>(false)
    const [userData, setUserData] = useState<UserDto>()
    const [focusTask, setFocusTask] = useState<TaskDto | null>(null)

    useEffect(() => {
        const data = userModel.getUserData()
        if(data) {
            setUserData(data)
            setFocusTask(userModel.getFocusTask())
        }
    }, [])

    const onOpenRegistModal = () => {
        if(!userData) {
            alert("로그인이 필요한 서비스 입니다.")
            return
        }
        else if(openRegistModal) return
        setOpenRegistModal(true)
    }
    const onCloseRegistModal = () => setOpenRegistModal(false)
    const onSelectFocusTask = (task: TaskDto) => {
        alert(`${task.title} 공략을 시작합니다!`)
        onCloseRegistModal()
        setFocusTask(task)
    }
    const onClearTask = async (progress: number) => {
        if(progress < 1 || !focusTask || !userData) {
            alert("잘못된 접근입니다.")
            return
        }

        focusTask.status = "DONE"
        const result = await userModel.updateTask(focusTask)
        if(result) {
            const afterTasks = userData!.myTodos.map(task => {
                if(task.id === focusTask.id) return focusTask
                return task
            })
            userModel.updateTasks(afterTasks, `${focusTask.title}의 집중공략을 성공적으로 끝냈습니다.`)
            userModel.removeFocusTask()
            alert("축하합니다! 처음 계획한 바를 이루셨나요?\n끝까지 완주하느라 고생하셨습니다!")
            
            setFocusTask(null)
            return
        }
        alert("요청에 실패했습니다.\n인터넷 환경을 원활하게 만들고 다시 시도해 주세요.")
        return
    }
    
    const getProgressVar = (end: Date) => {
        const start = new Date(Date.now())
        const startAt = start.getTime() / 1000
        const endAt = end.getTime() / 1000
        
        const day_dist = end.getDate() - start.getDate() < 0 ? end.getDate() : end.getDate() - start.getDate()
        const month_dist = (end.getMonth() + 1) - (start.getMonth() + 1) < 0 ? end.getMonth() : (end.getMonth() + 1) - (start.getMonth() + 1)
        const year_dist = end.getFullYear() - start.getFullYear()
        const has_dist = (day_dist + (month_dist * 30) + (year_dist * 365)) - 1 < 0 ? 0 : (day_dist + (month_dist * 30) + (year_dist * 365)) - 1
        
        return (has_dist + (1 - ((endAt - startAt) / 60 / 60 / 24))) / (has_dist + 1)
    }
    
    return (
        focusTask
        ? <ProgressBar 
          value={getProgressVar(new Date(focusTask.endAt))}
          onClearTask={onClearTask}
          />
        : <SelectFocusTask
          userData={userData}
          openRegistModal={openRegistModal}
          onCloseRegistModal={onCloseRegistModal}
          onOpenRegistModal={onOpenRegistModal}
          onSelectFocusTask={onSelectFocusTask}
          />
    )
}

import RunningIcon from "../../../../public/image/running.png"
import DoneIcon from "../../../../public/image/done.png"

function ProgressBar({ 
    value, 
    onClearTask,
}: { 
    value: number 
    onClearTask: (progress: number) => void
}) {
    const getProressText = () => {
        if(value >= 1) return "완벽하게 일정을 소화해냈어요!"
        else if(value >= .8) return "일정 마무리 까지 얼마 남지 않았어요!"
        else if(value >= .5) return "절반 이상 달려왔군요! 더 힘을 내요!"
        else return "집중공략 시작! 달려보아요!"
    }
    const getHurryUpText = () => {
        if(value >= 1) return "고생했어요!"
        else if(value >= .8) return "슬슬 마무리를 해야 할 시간이 왔어요!"
        else if(value >= .5) return "끈기 있게 끝까지 화이팅!"
        else return "시작이 반! 힘을 내서  앞으로 나아가요!"
    }
    return (
        <div className={styles.container}>
            <div className={styles.task_container}>
                <div className={styles.top}>
                    <span className={styles.title_text}>집중 공략중인 일정이에요!</span>
                    <TaskStatus progress={value}/>
                </div>
                <div className={styles.running}>
                    <span className={styles.progress_text}>{getProressText()}</span>
                    <Spacer spacing={5} direction="column"/>
                    <p>일정 이름</p>
                    <Spacer spacing={2.5} direction="column"/>
                    <Image
                    src={value >= 1 ? DoneIcon : RunningIcon}
                    className={styles.running_icon}
                    style={{ transform: `translateX(${482 * (value > 1 ? 1 : value)}px)`}}
                    width={18}
                    height={18}
                    alt="사람 아이콘"
                    />
                    <progress
                    max={1}
                    value={value}
                    />
                    <Spacer spacing={2.5} direction="column"/>
                    <p>{getHurryUpText()}</p>
                </div>
                {
                    value >= 1
                    ? <>
                        <TextButton
                        text="완료하기"
                        type="blue"
                        fontSize={12}
                        width={100}
                        onClick={() => onClearTask(value)}
                        />
                        <Spacer spacing={10} direction="column"/>
                      </>
                    : <></>
                }
            </div>
        </div>
    )
}

function SelectFocusTask({
    userData,
    openRegistModal,
    onCloseRegistModal,
    onOpenRegistModal,
    onSelectFocusTask,
}: SelectFocusTaskProps) {
    return (
        <div onClick={onOpenRegistModal} className={styles.container}>
            <span>집중공략</span>
            <Spacer spacing={10} direction="column"/>
            <div className={styles.content_container}>
                <Image
                src={ViewInArIcon}
                alt="집중공략 아이콘"
                />
                <Spacer spacing={10} direction="column"/>
                <span>집중적으로 해결하고 싶은 일을 등록 해보세요!</span>
            </div>
            {
                openRegistModal
                ? <RegistFocusTask 
                onSelectFocusTask={onSelectFocusTask} 
                onCloseRegistModal={onCloseRegistModal}
                tasks={userData?.myTodos ?? []}
                />
                : <></>
            }
        </div>
    )
}

interface SelectFocusTaskProps {
    onSelectFocusTask: (task: TaskDto) => void
    onCloseRegistModal: () => void
    onOpenRegistModal: () => void
    openRegistModal: boolean
    userData?: UserDto
}

function TaskStatus({ progress }: { progress: number }) {
    let status = "진행중"
    let color = "#00C8FF"

    if(progress >= 1) {
        status = "완료"
        color = "#24E34D"
    }

    return (
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
    )
}