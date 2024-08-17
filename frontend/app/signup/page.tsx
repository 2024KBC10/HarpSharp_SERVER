"use client";

import { useState } from "react"
import Link from "next/link"

import Spacer from "../(common)/(component)/(spacer)"

import styles from "./sign_in.module.css" 

export default function SignUp(){
    const [password, setPassword] = useState<string>("");
    const [confirmPassword, setConfirmPassword] = useState<string>("");
    return  (
            <div className={styles.container}>
                <div className={styles.inputdata}>
                    <h1 className={styles.headtext}>Sign-In</h1>
                    <input className={styles.input} placeholder="이메일"/>
                    <input
                    className={styles.input}
                    placeholder="비밀번호"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}/>
                    <input
                    className={styles.input}
                    placeholder="비밀번호 확인"
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}/>
                    <input className={styles.input} placeholder="이름"/>
                </div>
                <div className={styles.subitems}>
                    <button className={styles.signinbutton}>회원가입</button>
                    <div className={styles.subtext}>
                        <h1>이미 계정이 있으세요?</h1>
                        <Spacer spacing={10} direction="row"/>
                        <Link href="/login" legacyBehavior>
                            <a className={styles.detailtext}>로그인</a>
                        </Link>
                    </div>
                </div>
            </div>
        )
    }