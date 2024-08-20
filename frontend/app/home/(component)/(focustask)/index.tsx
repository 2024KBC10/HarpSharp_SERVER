import Image from "next/image"

import ViewInArIcon from "../../../../public/image/view_in_ar.png"
import styles from "./focus_task.module.css"
import Spacer from "@/app/(common)/(component)/(spacer)"

export default function FocusTask() {
    return (
        <div className={styles.container}>
            <span>집중공략</span>
            <Spacer spacing={10} direction="column"/>
            <div className={styles.content_container}>
                <Image
                src={ViewInArIcon}
                alt=""
                />
                <Spacer spacing={10} direction="column"/>
                <span>집중적으로 해결하고 싶은 일을 등록 해보세요!</span>
            </div>
        </div>
    )
}