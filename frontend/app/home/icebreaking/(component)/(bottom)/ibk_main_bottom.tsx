import { 
    IceBreakingDto, 
} from "../../../../(common)/(interface)/icebreaking.dto"

import { userModel } from "@/app/(common)/(model)"
import TextButton from "@/app/(common)/(component)/(button)"
import Question from "./(component)/(question)"
import Spacer from "@/app/(common)/(component)/(spacer)"

import styles from "./ibk_main_bottom.module.css"

export default function IBKMainBottom({
    questions,
    syncIBKQuestion,
    onOpenRegistTaskWindow,
}: IBKMainBottomProps) {
    return (
        <div className={styles.container}>
            <div className={styles.title_container}>
                <span>다른 질문 들</span>
                <div className={styles.action_container}>
                    <TextButton
                    text="질문 등록"
                    type="blue"
                    onClick={() => {
                        if(userModel.getUserData()) onOpenRegistTaskWindow()
                        else alert("로그인이 필요한 서비스 입니다.")
                    }}
                    fontSize={14}
                    width={80}
                    />
                    <Spacer spacing={10} direction="row"/>
                    <TextButton
                    text="질문 불러오기"
                    type="blue"
                    onClick={syncIBKQuestion}
                    fontSize={14}
                    width={100}
                    />
                </div>
            </div>
            {
                questions.length > 0
                ? <Questions questions={questions}/>
                : <div className={styles.text_wrapper}><span>아직 질문이 없어요!</span></div>
            }
        </div>
    )
}

function Questions({ questions }: { questions: IceBreakingDto[] }) {
    return (
        <ul className={styles.question_container}>
            {questions.map((question, index) => (
                <Question question={question} key={index}/>
            ))}
        </ul>
    )
}

interface IBKMainBottomProps {
    questions: IceBreakingDto[]
    syncIBKQuestion: () => void
    onOpenRegistTaskWindow: () => void
}