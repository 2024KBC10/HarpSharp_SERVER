import { getColor, getPinIconFromColor } from "../(registanswer)"
import { IceBreakingCommentDto } from "../../../../../(common)/(interface)/icebreaking.dto"
import AnswerDetail from "@/app/(common)/(component)/(answer)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./ibk_detail_top.module.css"

export default function IBKDetailBottom({
    replies,
}: IBKDetailBottomProps) {
    const col: IceBreakingCommentDto[][] = []
    
    let row: IceBreakingCommentDto[] = []
    let cnt = 0
    for(let i=0; i<replies.length; ++i) {
        ++cnt
        row.push(replies[i])
        if(cnt > 1) {
            col.push(row)
            row = []
            cnt = 0
        }
    }
    
    if(row.length > 0) {
        col.push(row)
        row = []
    }
    return (
        <ul className={styles.container}>
            {col.map((item, index) => (
                item.length > 1
                ? Double(item, index - 1)
                : Single(item[0], index - 1)
            ))}
        </ul>
    )
}

function Single(comment: IceBreakingCommentDto, key: number) {
    return (
        <li key={key + 1} className={styles.detail_single_wrapper}>
            <AnswerDetail comment={comment}/>
        </li>
    )
}

function Double(comments: IceBreakingCommentDto[], key: number) {
    return (
        <li key={key + 1} className={styles.detail_wrapper}>
            <AnswerDetail comment={comments[0]}/>
            <Spacer spacing={10}/>
            <AnswerDetail comment={comments[1]}/>
        </li>
    )
}

interface IBKDetailBottomProps {
    replies: IceBreakingCommentDto[]
}