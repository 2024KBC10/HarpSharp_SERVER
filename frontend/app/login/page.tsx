"use client";

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation";
import Link from "next/link"
import styles from "./log_in.module.css" 
import { APIManager } from "../(common)/(api)";
import Image from "next/image";
import SignatureIconRemoveBackground from "../../public/image/signature_icon_remove_background.png"

export default function Login(){
    const [stayStatus, setStayStatus] = useState<boolean>(false);
    const [username, setusername] = useState<string>("");
    const [inputPassword, setInputPassword] = useState<string>("");
    const [errorMessage, setErrorMessage] = useState<string>("");

    const router = useRouter();

    useEffect(() => {
        const token = localStorage.getItem("authToken");
        if(token){
            handleLogin(token, true);
        }
    }, []);

    useEffect(() => {
        if(!stayStatus){
            window.addEventListener("beforeunload", clearAuthToken);
            return () => {
                window.removeEventListener("beforeunload", clearAuthToken);
            };
        }
    }, [stayStatus]);

    const CheckboxChange = () =>{
        setStayStatus((prevState) => !prevState);
    };

    const clearAuthToken = () =>{
        localStorage.removeItem("authToken");
    };
    
    const handleLogin = async (existingToken?: string, autoLogin = false) =>{
        try{
            console.log(username, inputPassword);

            const response = await APIManager.post<{token: string }>({
                route: "login",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json;charset=UTF-8",
                    ...(existingToken? {Authorization: `Bearer ${existingToken}`} : {}),
                },
                body: existingToken ? undefined : {
                    username,
                    password: inputPassword,
                },
            });
            console.log("응답 전체: ", response);
            
            const headers = new Headers();
            headers.append("Authorization", response.headers.get("Authorization") || "");
        }
        catch(error){
            console.log("로그인 중 오류: ",error);
            if(!autoLogin){
                setErrorMessage("로그인 중 오류가 발생했습니다.");
            }
        }
    };
    {errorMessage && <p className={styles.error}>{errorMessage}</p>}

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
                        placeholder="이메일 또는 아이디"
                        value={username}
                        onChange={(e) => setusername(e.target.value)}
                        />
                        <input
                        className={styles.input}
                        placeholder="비밀번호"
                        type="password"
                        value={inputPassword}
                        onChange={(e) => setInputPassword(e.target.value)}/>
                        <button className={styles.loginbutton} onClick={() => handleLogin()}>로그인</button></div>
                <div className={styles.subitems}>
                    <div className={styles.staylogin}>
                        <input
                        type="checkbox"
                        checked={stayStatus}
                        onChange={CheckboxChange}
                        className={styles.staybutton}
                        />
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