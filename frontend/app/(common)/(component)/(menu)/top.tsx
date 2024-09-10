"use client"

import Image from "next/image"
import { useRouter } from "next/navigation"
import { UserDto } from "../../(interface)"

import Spacer from "../(spacer)"

import DefaultUserIcon from "../../../../public/image/default_user_icon.png"
import SignatureIcon from "../../../../public/image/signature_icon_grey.png"
import styles from "./top.module.css"
import { useEffect, useState } from "react"
import { APIManager } from "../../(api)"
import { userModel } from "../../(model)"

export default function MenuTop({
    userData,
}: MenuTopProps) {
    const router = useRouter()
    const onClickSignature = () => router.push("/home")

    const [userName, setUsername] = useState<string>("");
    const [createAtDate, setCreatedAtDate] = useState<string>("");
    const [position, setPosition] = useState<string>("");

    useEffect(() => {
        const fetchUserData = async () => {
            try{
                const token = localStorage.getItem("token");
                if(!token){
                    console.error("로그인 토큰이 없습니다.");
                    setUsername("로그인이 필요합니다");
                    return
                }

                const userData = await userModel.getUserData();

                if(userData){
                    setUsername(userData.username);
                    setPosition(userData.position);
                    const formattedDate = new Date(userData.createdAt).toLocaleDateString();
                    setCreatedAtDate(formattedDate);
                }
                else{
                    console.error("유저 데이터를 불러오는데 실패했습니다.");
                }
            }
            catch(error){
                console.error("유저 데이터를 가져오는 중 오류 발생 : ",error);
            }
        }
        fetchUserData();
    }, []);

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