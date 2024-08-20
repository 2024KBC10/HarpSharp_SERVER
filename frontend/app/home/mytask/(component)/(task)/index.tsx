import Image from "next/image"

import Spacer from "@/app/(common)/(component)/(spacer)"

import CalendarIcon from "../../../../../public/image/calendar.png"
import FlagIcon from "../../../../../public/image/flag.png"
import styles from "./task.module.css"

export default function Task({
    onChangeShowPage
}: TaskProps) {
    return (
        <li 
        className={styles.container}
        onClick={onChangeShowPage}
        >
            <span>일정 이름</span>
            <div className={styles.info_container}>
                <div className={styles.data_container}>
                    <Image
                    src={CalendarIcon}
                    alt="달력 아이콘"
                    />
                    <Spacer spacing={5} direction="row"/>
                    <div className={styles.data_wrapper}>
                        <p>목표 기한</p>
                        <p>0000년 0월 0일</p>
                    </div>
                </div>
                <div className={styles.data_container}>
                    <Image
                    src={FlagIcon}
                    alt="깃발 아이콘"
                    />
                    <Spacer spacing={5} direction="row"/>
                    <div className={styles.data_wrapper}>
                        <p>목표 달성까지</p>
                        <p>0개 남음</p>
                    </div>
                </div>
            </div>
        </li>
    )
}

interface TaskProps {
    onChangeShowPage: () => void
}