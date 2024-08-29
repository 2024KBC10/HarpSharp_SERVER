import styles from "./my_activity.module.css"

export default function MyActivity() {
    return (
        <div className={styles.container}>
            <div className={styles.title_wrapper}>
                <span>내 소식</span>
            </div>
            <div className={styles.activity_wrapper}>
                <span>아직 활동 기록이 없어요!</span>
            </div>
        </div>
    )
}