"use client"

import { useCallback, useEffect, useState } from "react"
import { useReRender } from "@/app/home/layout"
import { usePathname, useRouter } from "next/navigation"
import { userModel } from "../../(model)"
import { UserDto } from "../../(interface)"

import MenuTop from "./top"
import MenuBottom from "./bottom"
import MenuTab from "./tab"

import styles from "./fixed_menu.module.css"

export default function FixedMenu() {
    const [focusTab, setFocusTab] = useState<string>("")
    const [userData, setUserData] = useState<UserDto>()
    const { forceReRender } = useReRender()

    const logout = () => {
        userModel.logout()
        alert("로그아웃 되었습니다.")
        
        setUserData(undefined)
        forceReRender()
    }
    useEffect(() => { setUserData(userModel.getUserData() ?? undefined) }, [])
    
    const router = useRouter()
    const pathName = usePathname()

    const onClickTab = useCallback((tab: string) => {
        router.push(tab)
        setFocusTab(tab)
    }, [router])

    useEffect(() => {
        onClickTab(pathName) 
    }, [onClickTab, pathName])

    return (
        <div className={styles.container}>
            <div className={styles.top_container}>
                <MenuTop userData={userData}/>
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
                <MenuBottom onLogout={logout} isLogin={!!userData}/>
            </div>
        </div>
    )
}