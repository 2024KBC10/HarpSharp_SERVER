"use client"

import { 
    useCallback,
    useEffect, 
    useState 
} from "react"

import { TaskDto } from "../../(common)/(interface)/task.dto"
import { userModel } from "@/app/(common)/(model)"
import MyTaskMainBottom from "./(component)/(bottom)/main_bottom"
import MyTaskMainTop from "./(component)/(top)/main_top"
import RegistTask from "./(component)/(registtask)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./my_task.module.css"

export default function Mytask() {
    const [openRegistWindow, setOpenRegistWindow] = useState<boolean>(false)
    const [tasks, setTasks] = useState<TaskDto[]>([])
    const [isLogin, setIsLogin] = useState<boolean>(false)  

    const getTasks = async () => {
        try {
            const user = userModel.getUserData()
            setTasks(user!.myTodos)
        } catch(e) {
            alert("일정을 조회하는데 실패했습니다.\n원활한 통신 환경에 있는지 확인하고 재 시도 해주세요.")
            throw e
        }
    }
    const setTask = (task: TaskDto) => {
        const newTasks = tasks.map(item => {
            if(task.id === item.id) return task
            return item
        })
        const message = `${task.title}의 상태를 변경했습니다.`
        userModel.updateTasks(newTasks, message)

        alert(message)
        setTasks(newTasks)
    }
    const addTask = (task: TaskDto) => {
        const newTasks = [...tasks, task]
        const message = `${task.title}을 내 일정에 등록했습니다.`
        userModel.updateTasks(newTasks, message)

        alert("성공적으로 일정을 등록했습니다.")
        setOpenRegistWindow(false)
        setTasks(newTasks)
    }
    const removeTask = (task: TaskDto) => {
        const newTasks = tasks.filter(item => item.id !== task.id)
        const message = `${task.title}을 내 일정에서 삭제했습니다.`
        userModel.updateTasks(newTasks, message)
        alert("내 일정을 삭제했습니다.")
        setTasks(newTasks)
    }
    const initMyTasks = useCallback(() => {
        if(userModel.getUserData()) {
            getTasks()
            setIsLogin(true)
        }
    }, [])

    useEffect(() => { initMyTasks() }, [initMyTasks])

    return (
        <div className={styles.container}>
            <div className={styles.wrapper}>
                <MyTaskMainTop onOpenRegistWindow={() => setOpenRegistWindow(true)}/>
                {
                    isLogin
                    ? <>
                        <Spacer spacing={10} direction="column"/>
                        <MyTaskMainBottom onRefreshTasks={setTask} onRemoveTask={removeTask} tasks={tasks}/>
                        <RegistTask 
                        isOpen={openRegistWindow} 
                        onCloseRegistTaskWindow={() => setOpenRegistWindow(false)}
                        onAddTask={addTask}
                        />
                     </>
                    : <div className={styles.empty_wrapper}>
                        <span>로그인이 필요한 서비스 입니다</span>
                    </div>
                }
            </div>
        </div>
    )
}

/** Deprecated */
// {
//     showTaskPage
//     ? <div className={styles.wrapper}>
//         <MyTaskTop onChangeShowPage={() => setShowTaskPage(false)}/>
//         <MyTaskBottom/>
//       </div>
//     : <div className={styles.wrapper}>
//         <MyTaskMainTop onOpenRegistWindow={() => setOpenRegistWindow(true)}/>
//         <MyTaskMainBottom/>
//         <RegistTask isOpen={openRegistWindow} onCloseRegistTaskWindow={() => setOpenRegistWindow(false)}/>
//       </div>
// }