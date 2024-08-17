"use client"

import Image from "next/image"
import { useState } from "react"

import AISharkIcon from "../../../../public/image/AI_Icon.png"
import CloseAIIcon from "../../../../public/image/close_button.png"
import styles from "./ai_icon.module.css"
import AIPhone from "./ai_phone"

export default function AIIcon() {
    const [isOpen, setOpen] = useState<boolean>(false)
    const onOpenAI = () => setOpen(!isOpen)

    return (
        <div className={styles.container}>
            <AIPhone isOpen={isOpen}/>
            <Image
            className={styles.ai_icon_wrapper}
            onClick={onOpenAI}
            src={isOpen ? CloseAIIcon : AISharkIcon}
            alt="AI_Icon"
            width={60}
            height={60}
            />
        </div>
    )
}