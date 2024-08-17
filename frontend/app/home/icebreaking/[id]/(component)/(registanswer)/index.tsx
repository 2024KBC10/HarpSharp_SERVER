"use client"

import Image from "next/image"
import { useState } from "react"

import Spacer from "@/app/(common)/(component)/(spacer)"
import PreviewAnswer from "./(component)/(previewanswer)"
import TextButton from "@/app/(common)/(component)/(button)"

import CloseIcon from "../../../../../../public/image/close.png"
import styles from "./regist_answer.module.css"

export default function RegistAnswerModal({
    isShowing,
    onShowModal
}: RegistAnswerModalProps) {
    const [selMemoColor, setSelMemoColor] = useState<number>(0)
    const [selPinColor, setSelPinColor] = useState<number>(0)
    const [input, setInput] = useState<string>("")

    const memoColors: MemoColor[] = ["yellow", "purple", "pink", "sky-blue"]
    const pinColors: PinColor[] = ["brown", "bright-green", "bright-yellow", "bright-pink"]

    const onChangeText = (value: string) => {
        if(value.length <= 50) setInput(value)
    }

    return (
        <div 
        className={styles.container}
        style={{ display: isShowing ? "flex" : "none" }}
        >
            <Image
            src={CloseIcon}
            alt="닫기 버튼"
            width={12}
            height={12}
            className={styles.button}
            onClick={() => onShowModal(false)}
            />
            <span>답변 등록하기</span>
            <div className={styles.preview_container}>
                <PreviewAnswer 
                memo_color={getColor(memoColors[selMemoColor])}
                pin_icon={getPinIconFromColor(pinColors[selPinColor])}
                text={input}
                />
                <input 
                value={input}
                onChange={(e) => onChangeText(e.target.value)}
                placeholder="답변을 입력해 주세요"
                />
            </div>
            <div className={styles.action_container}>
                <div className={styles.select_color_container}>
                    <div className={styles.select_color_wrapper_col}>
                        <span>메모지 색상</span>
                        <Spacer spacing={5} direction="column"/>
                        <div className={styles.select_color_wrapper_row}>
                            {
                                memoColors
                                .map((color, index) => 
                                    (<SelectMemoColor 
                                    key={index}
                                    color={color}
                                    selectIndex={selMemoColor}
                                    index={index}
                                    onChangeMemoColor={setSelMemoColor}
                                    />)
                                )
                            }
                        </div>
                    </div>
                    <Spacer spacing={10} direction="column"/>
                    <div className={styles.select_color_wrapper_col}>
                        <span>핀 색상</span>
                        <Spacer spacing={5} direction="column"/>
                        <div className={styles.select_color_wrapper_row}>
                            {
                                pinColors
                                .map((color, index) => 
                                    (<SelectMemoColor
                                    key={index} 
                                    color={color}
                                    selectIndex={selPinColor}
                                    index={index}
                                    onChangeMemoColor={setSelPinColor}
                                    />)
                                )
                            }
                        </div>
                    </div>
                </div>
                <div className={styles.button_wrapper}>
                    <TextButton
                    text="답변 등록"
                    type="blue"
                    onClick={() => {}}
                    width={80}
                    height={20}
                    fontSize={14}
                    />
                </div>
            </div>
        </div>
    )
}

function SelectMemoColor({ 
    color,
    selectIndex,
    index,
    onChangeMemoColor,
}: Required<{ color: MemoColor | PinColor, selectIndex: number, index: number, onChangeMemoColor: (index: number) => void }>) {
    const memoColor = getColor(color)
    return (
        <div 
        className={styles.select_color}
        style={{ 
            backgroundColor: `${memoColor}`,
            boxShadow: `0px 4px 4px 0px rgba(0,0,0,${selectIndex === index ? .25 : 0})` 
        }}
        onClick={() => onChangeMemoColor(index)}
        />
    )
}

function getColor(color: MemoColor | PinColor) {
    switch(color) {
        case "yellow": return "#FFFA5E"
        case "purple": return "#B45EFF"
        case "pink": return "#FF5EC4"
        case "sky-blue": return "#5EA3FF"  
        case "brown": return "#F5A953"
        case "bright-green": return "#E7FF5E"
        case "bright-pink": return "#FF5E5E"
        case "bright-yellow": return "#5EFF8E"
        default: throw new Error("잘못 된 접근 입니다.")
    }
}

import BrownPinIcon from "../../../../../../public/image/pin_default.png"
import BrightGreenPinIcon from "../../../../../../public/image/pin_bright_green.png"
import BrightPinkPinIcon from "../../../../../../public/image/pin_pink.png"
import BrightYellowPinIcon from "../../../../../../public/image/pin_bright_yellow.png"

function getPinIconFromColor(color: PinColor) {
    switch(color) {
        case "brown": return BrownPinIcon
        case "bright-green": return BrightGreenPinIcon
        case "bright-pink": return BrightPinkPinIcon
        case "bright-yellow": return BrightYellowPinIcon
        default: throw new Error("잘못 된 접근 입니다.")
    }
}

interface RegistAnswerModalProps {
    isShowing: boolean
    onShowModal: (value: boolean) => void
}

type MemoColor = "yellow" | "purple" | "pink" | "sky-blue"
type PinColor = "brown" | "bright-green" | "bright-yellow" | "bright-pink"