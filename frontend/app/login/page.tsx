"use client";

import { useState } from "react"
import Link from "next/link"

import styles from "./log_in.module.css" 

export default function Login(){
    const [stayStatus, setStayStatus] = useState<boolean>(false);
    const CheckboxChange = () =>{
        setStayStatus((prevState) => !prevState);
    };
    const [inputPassword, setInputPassword] = useState<string>("");

    return  (
            <div className={styles.container}>
                <div className={styles.inputdata}>
                    <h1 className={styles.headtext}>Log-In</h1>
                        <input className={styles.input} placeholder="이메일 또는 아이디"/>
                        <input
                        className={styles.input}
                        placeholder="비밀번호"
                        type="password"
                        value={inputPassword}
                        onChange={(e) => setInputPassword(e.target.value)}/>
                        <button className={styles.loginbutton}>로그인</button></div>
                <div className={styles.subitems}>
                    <div className={styles.staylogin}>
                        <input type="checkbox" checked={stayStatus} onChange={CheckboxChange} className={styles.staybutton}/>
                        <h1 className={styles.text}>로그인 상태 유지</h1>
                    </div>
                    <div className={styles.extraoption}>
                        <Link href="/signup" legacyBehavior>
                            <a className={styles.optiontext}>회원가입</a>
                        </Link>
                    </div>
                </div>
            </div>
        )
    }