"use client"

import Image from "next/image"

import SignUpIcon from "../../../../public/image/sign_up.png"
import styles from "./bottom.module.css"
import Link from "next/link"

export default function MenuBottom() {
    return (
        <div className={styles.container}>
            <div className={styles.button_container}>
                <div className={styles.description_wrapper}>
                    <span>로그인</span>
                </div>
                <div className={styles.button_wrapper}>
                    <Link href="/login">
                        <Image
                        src={SignUpIcon}
                        alt="로그인 버튼"
                        />
                    </Link>
                </div>
            </div>
        </div>
    )
}
