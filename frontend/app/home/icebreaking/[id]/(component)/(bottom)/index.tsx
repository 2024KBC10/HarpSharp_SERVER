import AnswerDetail from "@/app/(common)/(component)/(answer)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./ibk_detail_top.module.css"

export default function IBKDetailBottom() {
    return (
        <ul className={styles.container}>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>

            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
            <div className={styles.detail_wrapper}>
                <AnswerDetail/>
                <Spacer spacing={10}/>
                <AnswerDetail/>
            </div>
        </ul>
    )
}