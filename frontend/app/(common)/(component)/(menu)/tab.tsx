import Spacer from "../(spacer)"

import styles from "./tab.module.css"

export default function MenuTab({
    onClick,
    isFocus,
    name,
    route,
}: MenuTabProps) {
    return (
        <div 
        onClick={() => onClick(route)}
        className={isFocus ? styles.container_focus : styles.container_unfocus}
        >
            <Spacer spacing={5}/>
            <span># {name}</span>
        </div>
    )
}

interface MenuTabProps {
    onClick: (tab: string) => void
    isFocus: boolean
    name: string
    route: string
}