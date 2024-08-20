"use client"

import { useState } from "react"

import TextButton from "@/app/(common)/(component)/(button)"
import Spacer from "@/app/(common)/(component)/(spacer)"
import TaskFilter from "../(filter)"

import DropdownArrow from "../../../../../public/image/arrow_down.png"
import styles from "./main_top.module.css"

export default function MyTaskMainTop({
    onOpenRegistWindow,
}: MyTaskTopProps) {
    const [openFilterFromDate, setOpenFilterFromDate] = useState<boolean>(false)
    const [openFilterFromState, setOpenFilterFromState] = useState<boolean>(false)
    const [fromDateTarget, setFromDateTarget] = useState<FilterFromDate>("시작순")
    const [fromStateTarget, setFromStateTarget] = useState<FilterFromState>("모두")

    const filter = (target: string | FilterFromDate | FilterFromState) => {
        switch(target) {
            case "시작순":
            case "종료순":
            case "작업순":
                setFromDateTarget(target as FilterFromDate)
                break
            case "모두":
            case "진행중":
            case "중단":
            case "완료":
                setFromStateTarget(target as FilterFromState)
                break
            default: return
        }
    }
    
    const fromDate = [
        {
            name: "시작순",
            onClick: filter,
        },
        {
            name: "종료순",
            onClick: filter,
        },
        {
            name: "작업순",
            onClick: filter,
        }
    ]

    const fromState = [
        {
            name: "모두",
            onClick: filter,
        },
        {
            name: "진행중",
            onClick: filter,
        },
        {
            name: "중단",
            onClick: filter,
        },
        {
            name: "완료",
            onClick: filter,
        }
    ]

    return (
        <div className={styles.container}>
            <span>내 일정</span>
            <div className={styles.feature_cotainer}>
                <div className={styles.filter_container}>
                    <TextButton 
                    text={fromStateTarget}
                    borderRadius={5}
                    prefixIcon={{ icon: DropdownArrow, rotateOnClick: true }}
                    onClick={() => setOpenFilterFromState(!openFilterFromState)}
                    />
                    <TaskFilter
                    items={fromState}
                    isOpen={openFilterFromState}
                    />
                </div>
                <Spacer spacing={10} direction="row"/>
                <div className={styles.filter_container}>
                    <TextButton 
                    text={fromDateTarget}
                    borderRadius={5}
                    prefixIcon={{ icon: DropdownArrow, rotateOnClick: true }}
                    onClick={() => setOpenFilterFromDate(!openFilterFromDate)}
                    />
                    <TaskFilter 
                    isOpen={openFilterFromDate} 
                    items={fromDate}
                    />
                </div>
                <Spacer spacing={10} direction="row"/>
                <TextButton 
                text="일정 만들기"
                width={100}
                type="blue"
                borderRadius={5}
                onClick={onOpenRegistWindow}
                />
            </div>
        </div>
    )
}

interface MyTaskTopProps {
    onOpenRegistWindow: () => void
}

type FilterFromDate = "시작순" | "종료순" | "남은 작업 순"
type FilterFromState = "모두" | "진행중" | "중단" | "완료"