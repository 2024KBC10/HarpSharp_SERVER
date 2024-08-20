"use client"

import Image from "next/image"
import { useRouter } from "next/navigation"

import Spacer from "../(spacer)"

import DefaultUserIcon from "../../../../public/image/default_user_icon.png"
import SignatureIcon from "../../../../public/image/signature_icon_grey.png"
import styles from "./top.module.css"

export default function MenuTop() {
    const router = useRouter()
    const onClickSignature = () => router.push("/")
    return (
        <div className={styles.container}>
            <Spacer spacing={5} direction="column"/>
            <div className={styles.title} onClick={onClickSignature}>
                <Image
                src={SignatureIcon}
                alt="시그니쳐 이미지"
                />
                <Spacer spacing={5}/>
                <span>KBC-OFFICE</span>
            </div>
            <Spacer spacing={10} direction="column"/>
            <div className={styles.info}>
                <Image
                src={DefaultUserIcon}
                alt="사용자 기본 이미지"
                style={{ alignSelf: "flex-start" }}
                />
                <Spacer spacing={10} direction="row"/>
                <div className={styles.name_container}>
                    <span className={styles.name}>한강민</span>
                    <Spacer spacing={2.5} direction="column"/>
                    <span className={styles.nickname}>kangmin.han(한강민) / 클라우드</span>
                </div>
            </div>
            <Spacer spacing={5} direction="column"/>
            <span style={{ alignSelf: "flex-end" }}>가입일 2024.07.20</span>
        </div>
    )
}