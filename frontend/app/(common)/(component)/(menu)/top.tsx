"use client"

import Image from "next/image"
import { useRouter } from "next/navigation"
import { UserDto } from "../../(interface)"

import Spacer from "../(spacer)"

import DefaultUserIcon from "../../../../public/image/default_user_icon.png"
import SignatureIcon from "../../../../public/image/signature_icon_grey.png"
import styles from "./top.module.css"

export default function MenuTop({
    userData,
}: MenuTopProps) {
    const router = useRouter()
    const onClickSignature = () => router.push("/home")

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
                    <span className={styles.name}>{userData?.username ?? "게스트"}</span>
                    <Spacer spacing={2.5} direction="column"/>
                    <span className={styles.nickname}>{userData?.username ?? "게스트"} / {userData?.position ?? "손님"}</span>
                </div>
            </div>
            <Spacer spacing={5} direction="column"/>
            <span style={{ alignSelf: "flex-end" }}>{ userData?.createdAt ? `가입일 ${Intl.DateTimeFormat("ko", { dateStyle: "full" }).format(new Date(userData.createdAt))}` : "" }</span>
        </div>
    )
}

interface MenuTopProps {
    userData?: UserDto
}