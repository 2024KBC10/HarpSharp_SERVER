"use client"

import { useState } from "react"

import MyTaskMainBottom from "./(component)/(bottom)/main_bottom"
import MyTaskMainTop from "./(component)/(top)/main_top"
import RegistTask from "./(component)/(registtask)"
import MyTaskTop from "./(component)/(top)/task_top"
import MyTaskBottom from "./(component)/(bottom)/task_bottom"

import styles from "./my_task.module.css"

export default function Mytask() {
    const [showTaskPage, setShowTaskPage] = useState<boolean>(false)
    const [openRegistWindow, setOpenRegistWindow] = useState<boolean>(false)
    return (
        <div className={styles.container}>
            {
                showTaskPage
                ? <div className={styles.wrapper}>
                    <MyTaskTop onChangeShowPage={() => setShowTaskPage(false)}/>
                    <MyTaskBottom/>
                  </div>
                : <div className={styles.wrapper}>
                    <MyTaskMainTop onOpenRegistWindow={() => setOpenRegistWindow(true)}/>
                    <MyTaskMainBottom onChangeShowPage={() => setShowTaskPage(true)}/>
                    <RegistTask isOpen={openRegistWindow} onCloseRegistTaskWindow={() => setOpenRegistWindow(false)}/>
                  </div>
            }
        </div>
    )
}