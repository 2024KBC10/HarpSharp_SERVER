import Image from "next/image"
import SignUpIcon from "../../../../public/image/sign_up.png"
import SignOutIcon from "../../../../public/image/sign_out.png"
import styles from "./bottom.module.css"
import Link from "next/link"

export default function MenuBottom({
    isLogin,
    onLogout
}: MenuBottomProps) {
    return (
        <div className={styles.container}>
            <div className={styles.button_container}>
                <div className={styles.description_wrapper}>
                    <span>{isLogin ? "로그아웃" : "로그인"}</span>
                </div>
                <div className={styles.button_wrapper}>
                    {
                        isLogin
                        ?  <Image 
                            src={SignOutIcon} 
                            onClick={onLogout}
                            alt="로그아웃 버튼"
                           /> 
                        :  <Link href="/login">
                            <Image
                            src={SignUpIcon}
                            alt="로그인 버튼"
                            />
                          </Link>
                    }
                </div>
            </div>
        </div>
    );
}

interface MenuBottomProps {
    isLogin: boolean
    onLogout: () => void
}