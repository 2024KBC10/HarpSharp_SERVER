import Image from "next/image"

import Spacer from "@/app/(common)/(component)/(spacer)"
import TextButton from "@/app/(common)/(component)/(button)"

import ArrowIcon from "../../../../../public/image/arrow_down.png"
import styles from "./task_top.module.css"

export default function MyTaskTop({
    onChangeShowPage,
}: MyTaskTopProps) {
    return (
        <div className={styles.container}>
            <div className={styles.title_action_container}>
                <div className={styles.title_container}>
                    <div 
                    onClick={onChangeShowPage}
                    className={styles.back_button_wrapper}>
                        <Image
                        src={ArrowIcon}
                        alt="뒤로가기 아이콘"
                        className={styles.back_icon}
                        />
                    </div>
                    <Spacer spacing={10} direction="row"/>
                    <span>일정 이름</span>
                </div>
                <TextButton
                text="저장"
                type="blue"
                onClick={() => {}}
                width={90}
                height={15}
                fontSize={14}
                />
            </div>
            <div className={styles.memo_container}>
                <div style={{ marginLeft: "10px" }}><span>메모</span></div>
                <Spacer spacing={5} direction="column"/>
                <p style={{ wordWrap: "break-word", wordBreak: "keep-all" }}>
                ntentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent
                </p>
            </div>
            <div className={styles.info_container}>
                <div className={styles.info_data_container}>
                    <span>시작</span>
                    <p>0000년 0월 0일</p>
                </div>
                <div className={styles.info_data_container}>
                    <span>종료</span>
                    <p>0000년 0월 0일</p>
                </div>
                <div className={styles.info_data_container}>
                    <span>상태</span>
                    <p>진행중</p>
                </div>
            </div>
        </div>
    )
}

interface MyTaskTopProps {
    onChangeShowPage: () => void
}

// 글자수 제한 300자