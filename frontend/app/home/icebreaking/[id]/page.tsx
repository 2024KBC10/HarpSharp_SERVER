"use client"

import { useState } from "react"

import IBKDetailBottom from "./(component)/(bottom)"
import RegistAnswerModal from "./(component)/(registanswer)"
import IBKDetailTop from "./(component)/(top)"

import styles from "./ibk_detail.module.css"

export default function IBKDetail() {
    const [showRegistAnswerModal, setShowRegistAnswerModal] = useState<boolean>(false)

    return (
        <div className={styles.container}>
            <IBKDetailTop onShowModal={setShowRegistAnswerModal}/>
            <IBKDetailBottom/>
            {
                showRegistAnswerModal
                ?   <RegistAnswerModal 
                    onShowModal={setShowRegistAnswerModal} 
                    isShowing={showRegistAnswerModal}
                    />
                :   <></>
            }
        </div>
    )
}