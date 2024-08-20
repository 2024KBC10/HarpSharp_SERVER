"use client"

import Image, { 
    StaticImageData 
} from "next/image"

import styles from "./preview_answer.module.css"

export default function PreviewAnswer({
    memo_color,
    pin_icon,
    text,
}: PreviewAnswerProps) {
    return (
        <div className={styles.container}>
            <Image
            src={pin_icon.src}
            alt="핀 아이콘"
            width={18}
            height={18}
            className={styles.pin}
            />
            <div 
            className={styles.field_container}
            style={{ backgroundColor: memo_color }}
            >
                <p>{text}</p>
            </div>
        </div>
    )
}

interface PreviewAnswerProps {
    memo_color: string
    pin_icon: StaticImageData
    text: string
}