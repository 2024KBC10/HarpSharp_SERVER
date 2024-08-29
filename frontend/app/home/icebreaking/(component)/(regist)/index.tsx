"use client"

import { useState } from "react"
import Image from "next/image"

import TextButton from "@/app/(common)/(component)/(button)"
import InputField from "@/app/(common)/(component)/(inputfield)"
import { 
    IceBreakingDto, 
    IceBreakingJson 
} from "../../../../(common)/(interface)/icebreaking.dto"
import { APIManager } from "@/app/(common)/(api)"
import { LocalStorage } from "@/app/(common)/(storage)"
import { IceBreakingProvider } from "../../../../(common)/(provider)/icebreaking.provider"
import { userModel } from "@/app/(common)/(model)"

import CloseIcon from "../../../../../public/image/close.png"
import styles from "./regist_ibk.module.css"

export default function RegistIBK({
    isOpen,
    onAddIBKQuestion,
    onCloseRegistTaskWindow,
}: RegistIBKProps) {
    const [title, setTitle] = useState<string>("")
    const [content, setContent] = useState<string>("")

    const registQuestion = async () => {
        try {
            const user = userModel.getUserData()
            if(user) {
                // 이 부분에 대해 refresh 토큰까지 거치고 로그아웃 핸들링이 필요 함
                const token = LocalStorage.get('token')
                const result = await APIManager.post<IceBreakingJson>({
                    route: "/board/posts",
                    body: {
                        username: user.username,
                        title,
                        content,
                    },
                    headers: {
                        Authorization: `${token}`
                    }
                })
                
                if("error" in result) {
                    alert("질문 등록에 실패했습니다.\n입력한 정보가 올바른지 다시 확인 해주세요.")
                    return
                }
                const question_id = Object.keys(result.data!)[0]
                const question = IceBreakingProvider.toDto(question_id, result.data![question_id])
                alert(result.details)
                onCloseRegistTaskWindow()
                onAddIBKQuestion(question)
            } else alert("로그인이 필요한 서비스 입니다.")
        } catch(e) {
            alert("질문 등록에 실패했습니다.\n원활한 통신 환경에 있는지 확인하고 재 시도 해주세요.")
            return
        }
    }
    return (
        <div 
        className={styles.container}
        style={{  width: isOpen ? "30%" : "0px" }}
        >
            <div 
            className={styles.top_container}
            style={{  
                width: isOpen ? "100%" : "0px", 
                opacity: isOpen ? 1 : 0,
                display: isOpen? "flex" : "none",
            }}
            >
                <span>질문 등록하기</span>
                <Image
                src={CloseIcon}
                width={16}
                height={16}
                onClick={onCloseRegistTaskWindow}
                className={styles.close}
                alt="닫기 아이콘"
                />
            </div>
            <div 
            className={styles.body_container}
            style={{  
                width: isOpen ? "100%" : "0px", 
                opacity: isOpen ? 1 : 0,
                display: isOpen? "flex" : "none",
            }}
            >
                <div className={styles.input_container}>
                    <InputField
                    category="질문 이름"
                    placeholder="질문 이름을 입력해주세요."
                    onChange={setTitle}
                    isRequired={true}
                    />
                    <InputField
                    category="질문 내용"
                    placeholder="질문 내용을 입력해주세요."
                    onChange={setContent}
                    isRequired={true}
                    />
                </div>
                <div className={styles.button_wrapper}>
                    <TextButton
                    text="질문 등록"
                    type="blue"
                    onClick={registQuestion}
                    borderRadius={5}
                    />
                </div>
            </div>
        </div>
    )
}

interface RegistIBKProps {
    isOpen: boolean
    onAddIBKQuestion: (question: IceBreakingDto) => void
    onCloseRegistTaskWindow: () => void
}