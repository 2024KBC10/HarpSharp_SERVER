"use client"

import Image, { 
    StaticImageData 
} from "next/image"
import { useState } from "react"

import styles from "./button.module.css"

export default function TextButton({
    text,
    width = 80,
    fontSize = 16,
    fontWeigth = "400",
    borderRadius = 10,
    type = "default",
    prefixIcon,
    height,
    onClick = () => {},
}: TextButtonProps) {
    const [angle, setAngle] = useState<number>(0)
    
    const icon : JSX.Element | undefined = 
    prefixIcon 
    ? <Image 
        src={prefixIcon.icon} 
        alt="아이콘" 
        width={prefixIcon.width} 
        height={prefixIcon.height}
        className={styles.button_icon}
        style={{ transform: `rotate(${angle}turn)` }}
    /> 
    : undefined

    return (
        <div 
        className={styles[`container_${type}`]}
        onClick={() => {
            if(prefixIcon && prefixIcon.rotateOnClick) setAngle(angle === 0 ? 0.5 : 0)
            onClick()
        }}
        style={{
            width: `${width}px`,
            height: `${height}px`,
            borderRadius: `${borderRadius}px`,
            fontSize: `${fontSize}px`,
            fontWeight: fontWeigth,
            justifyContent: icon === undefined ? "center" : "space-between",
        }}
        >{text}{icon}</div>
    )
}

interface TextButtonProps {
    text: string
    onClick: () => void
    type?: "default" | "blue"
    borderRadius?: number
    fontSize?: number
    fontWeigth?: "400" | "500" | "bold"
    prefixIcon?: PrefixIconProps
    width?: number
    height?: number
}

interface PrefixIconProps {
    icon: StaticImageData
    width?: number
    height?: number
    rotateOnClick?: boolean
}