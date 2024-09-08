"use client"

import { 
    useCallback, 
    useEffect,
    useState 
} from "react"

import { 
    IceBreakingDto, 
    IceBreakingJson 
} from "../../(common)/(interface)/icebreaking.dto"
import { IceBreakingProvider } from "../../(common)/(provider)"
import { SessionStorage } from "@/app/(common)/(storage)"
import { APIManager } from "@/app/(common)/(api)"
import { userModel } from "@/app/(common)/(model)"
import RegistIBK from "./(component)/(regist)"
import IBKMainBottom from "./(component)/(bottom)/ibk_main_bottom"

import styles from "./page.module.css"

export default function IceBreaking() {
    const [openRegistWindow, setOpenRegistWindow] = useState<boolean>(false)
    const [questions, setQuestions] = useState<IceBreakingDto[]>([])
    
    const getIBKQuestion = async () => {
        try {
            return await APIManager.get<IceBreakingJson>({
                route: "/board/posts",
            })
            .then(res => {
                const arr: IceBreakingDto[] = []
                if(typeof res !== "boolean") {
                    if("error" in res) return arr
                    const keys = Object.keys(res)
                    if(keys.length > 0) {
                        for(let i=0; i<keys.length; ++i) {
                            const key = keys[i]
                            arr
                            .push(
                                IceBreakingProvider
                                .toDto(key, res[key])
                            )    
                        }
                    }
                }
                return arr
            })
        } catch(e) { 
            alert("질문을 조회하는데 실패했습니다.\n원활한 통신 환경에 있는지 확인하고 재 시도 해주세요.")
            throw e
        }
    }

    const addIBKQuestion = (question: IceBreakingDto) => {
        const key = "ibk_posts"
        const cache: IceBreakingDto[] = JSON.parse(SessionStorage.get(key) ?? "[]")
        const newQuestions = [question, ...cache]

        SessionStorage.set(key, JSON.stringify(newQuestions))
        userModel.addLog(`아이스 브레이킹 ${question.title}을(를) 등록했습니다.`)
        setQuestions(newQuestions)
    }

    // 주어진 기준 상세화 필요
    const sortQuestions = (
        questions: IceBreakingDto[],
    ) => {
        return questions.sort((a, b) => {
            if(a.createdAt > b.createdAt) return -1
            else if(a.createdAt < b.createdAt) return 1
            return 0
        })
    }
    
    const syncIBKQuestion = async () => {
        try {
            const key = "ibk_posts"
            const questions = await getIBKQuestion()
            SessionStorage.set(key, JSON.stringify(questions))
            setQuestions(sortQuestions(questions))
        } catch(e) { return }
    }

    /**
     * 질문에 달린 답변들은 question_id 별로 관리
     */
    const initIBKQuestion = useCallback(async () => {
        try {
            const question_key = "ibk_posts"
            const questions_cache = SessionStorage.get(question_key)
            
            if(questions_cache) {
                const questions : IceBreakingDto[] = JSON.parse(questions_cache)
                setQuestions(sortQuestions(questions))
            } else {
                const questions = await getIBKQuestion()
                SessionStorage.set(question_key, JSON.stringify(questions))
                setQuestions(sortQuestions(questions))
            }
        } catch(e) { return }
    }, [setQuestions])

    useEffect(() => {initIBKQuestion()}, [initIBKQuestion])
    return (
        <div className={styles.container}>
            {/* <IBKMainTop/> */}
            <IBKMainBottom 
            questions={questions} 
            syncIBKQuestion={() => syncIBKQuestion()}
            onOpenRegistTaskWindow={() => setOpenRegistWindow(true)}
            />
            <RegistIBK 
            onAddIBKQuestion={addIBKQuestion}
            onCloseRegistTaskWindow={() => setOpenRegistWindow(false)} 
            isOpen={openRegistWindow}
            />
        </div>
    )
}