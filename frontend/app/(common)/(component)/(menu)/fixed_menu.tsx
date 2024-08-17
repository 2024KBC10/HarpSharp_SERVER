"use client"

import { useCallback, useEffect, useState } from "react"
import { usePathname, useRouter } from "next/navigation"

import MenuTop from "./top"
import MenuBottom from "./bottom"
import MenuTab from "./tab"

import styles from "./fixed_menu.module.css"

export default function FixedMenu() {
    const [focusTab, setFocusTab] = useState<string>("")
    
    const router = useRouter()
    const pathName = usePathname()

    const onClickTab = useCallback((tab: string) => {
        router.push(tab)
        setFocusTab(tab)
    }, [router])

    useEffect(() => { onClickTab(pathName) }, [onClickTab, pathName])

    return (
        <div className={styles.container}>
            <div className={styles.top_container}>
                <MenuTop/>
            </div>
            <ul className={styles.tab_container}>
                <MenuTab 
                onClick={onClickTab}
                isFocus={focusTab === "/home"}
                name="홈"
                route="/home"
                />
                <MenuTab 
                onClick={onClickTab}
                isFocus={focusTab === "/home/mytask"}
                name="내 일정"
                route="/home/mytask"
                />
                <MenuTab 
                onClick={onClickTab}
                isFocus={focusTab === "/home/icebreaking"}
                name="아이스 브레이킹"
                route="/home/icebreaking"
                />
            </ul>
            <div className={styles.bottom_container}>
                <MenuBottom/>
            </div>
        </div>
    )
}