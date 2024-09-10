"use client";

import { useState } from "react"
import { useRouter } from "next/navigation";
import Link from "next/link"
import Image from "next/image";

import { Position, userModel } from "../(common)/(model)";

import Spacer from "../(common)/(component)/(spacer)"
import styles from "./sign_in.module.css" 

import SignatureIconRemoveBackground from "../../public/image/signature_icon_remove_background.png"

export default function SignUp(){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [username, setName] = useState("");
    const [passwordMatch, setPasswordMatch] = useState<null | boolean>(null);
    const [errorMessage, setErrorMessage] = useState("");
    const [position, setPosition] = useState<string>("CLOUD")

    const router = useRouter()

    const handlePasswordMatchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
        setPasswordMatch(e.target.value === confirmPassword);
    };

    const handleConfirmPasswordMatchChange = (e: React.ChangeEvent<HTMLInputElement>) =>{
        setConfirmPassword(e.target.value);
        setPasswordMatch(e.target.value === password);
    }

    const upperCasePosition = position.toUpperCase();

    const handleSignup = async () => {
        if(password !== confirmPassword){
            setErrorMessage("비밀번호가 일치하지 않습니다.");
            return;
        }
        try{
            const result = await userModel.signUp({
                email,
                password,
                username,
                position: upperCasePosition as Position,
            })
            if(result) {
                alert("축하합니다! 회원가입에 성공했습니다.\n로그인 페이지로 이동합니다.")
                router.push("/login")
                return
            }
            alert("회원가입에 실패했습니다.\n입력하신 정보를 다시 한 번 확인 해 주세요.")
        } catch(e){
            console.log(e)
            alert("회원가입에 실패했습니다.\n입력하신 정보를 다시 한 번 확인 해 주세요.")
            return
        }
    };
    return  (
            <div className={styles.container}>
                <div className={styles.inputdata}>
                    <Link href="/home">
                        <Image
                        src={SignatureIconRemoveBackground}
                        alt="홈으로 이동"
                        width={150}
                        height={150}
                        className={styles.headimage}
                        />
                    </Link>
                    <input
                    className={styles.input}
                    placeholder="이메일"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    />
                    <input
                    className={`${styles.input} ${
                        passwordMatch === true ? styles.passwordmatch : passwordMatch === false ? styles.passwordmismatch : ""
                    }`}
                    placeholder="비밀번호"
                    type="password"
                    value={password}
                    onChange={handlePasswordMatchChange}/>
                    <input
                    className={`${styles.input} ${
                        passwordMatch === true ? styles.passwordmatch : passwordMatch === false ? styles.passwordmismatch : ""
                    }`}
                    placeholder="비밀번호 확인"
                    type="password"
                    value={confirmPassword}
                    onChange={handleConfirmPasswordMatchChange}
                    />
                    <input
                    className={styles.input}
                    placeholder="아이디"
                    value={username}
                    onChange={(e) => setName(e.target.value)}
                    />
                    <select
                    className={styles.select}
                    value={position}
                    onChange={(e) => setPosition(e.target.value)}
                    >
                        <option value = "" disabled>과정 선택</option>
                        <option value = "CLOUD">클라우드</option>
                        <option value = "FULLSTACK">풀스택</option>
                        <option value = "AI">AI</option>
                    </select>
                    {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                </div>
                <div className={styles.subitems}>
                    <button className={styles.signinbutton} onClick={handleSignup}>회원가입</button>
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
