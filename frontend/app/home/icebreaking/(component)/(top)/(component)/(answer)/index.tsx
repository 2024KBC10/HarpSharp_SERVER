import Image from "next/image"

import PinIcon from "../../../../../../../public/image/pin_default.png"
import styles from "./answer.module.css"

export default function Answer() {
    return (
        <div className={styles.container}>
            <Image
            src={PinIcon}
            alt="핀 아이콘"
            className={styles.pin}
            width={12}
            height={12}
            />
            <div className={styles.content_container}>
                <p>contentcontentcontentcontentcontentconcontentcontentcontentcontentcontentcon</p>
            </div>
        </div>
    )
}