import Question from "./(component)/(question)"
import styles from "./ibk_main_bottom.module.css"

export default function IBKMainBottom() {
    return (
        <div className={styles.container}>
            <span>다른 질문 들</span>
            <ul className={styles.question_container}>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
                <Question/>
            </ul>
        </div>
    )
}