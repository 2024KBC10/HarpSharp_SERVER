import Tasks from "../(tasks)"
import styles from "./main_bottom.module.css"

export default function MyTaskMainBottom({
    onChangeShowPage
}: MyTaskMainBottom) {
    return (
        <div className={styles.container}>
            <Tasks onChangeShowPage={onChangeShowPage} type="progress"/>
            <Tasks onChangeShowPage={onChangeShowPage} type="stop"/>
            <Tasks onChangeShowPage={onChangeShowPage} type="success"/>
        </div>
    )
}

interface MyTaskMainBottom {
    onChangeShowPage: () => void
}