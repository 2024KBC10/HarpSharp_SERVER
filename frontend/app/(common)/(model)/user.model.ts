import { 
    UserDto, 
    UserJson 
} from "../(interface)";
import { APIManager } from "../(api)";
import { LocalStorage } from "../(storage)";
import { UserProvider } from "../(provider)";

class UserModel {
    private userData: UserDto | null = null

    public getUserData() { return this.userData }

    public async login(args: IUserLoginArgs): Promise<boolean> {
        const response = await APIManager.post<UserJson>({
            route: "/login",
            body:{
                username: args.username,
                password: args.password,
            },
        });
        if("data" in response && response.data){
            const token = response.authorization
            if(token) {
                const key = Object.keys(response.data)[0]
                this.userData = UserProvider.userinfoDto(key, response.data[key])
                LocalStorage.set("token", token)
            }
            else throw new Error("로그인 실패: 응답에 토큰이 없습니다.")
            return true
        }

        return false
    }

    public async signUp(args: IUserSignUpArgs): Promise<boolean> {
        const response = await APIManager.post({
            route: "/join",
            body: {
                email: args.email,
                username: args.username,
                password: args.password,
            }
        })

        if("message" in response && response.code === 201) return true
        return false
    }
    
    /** s3 이미지 업로드 관련 미사용 중 */
    // private async uploadProfileImage(file: File): Promise<string | "failed"> {
    //     const response = await APIManager.post({
    //         route: "/",
    //         body: {
                
    //         },
    //         headers: { "Content-Type": "application/octet-stream" }
    //     })

    //     if(response.code === 200) return ""
    //     return "failed"
    // }
}

export const userModel = new UserModel()

interface IUserLoginArgs {
    readonly username: string
    readonly password: string
}

interface IUserSignUpArgs {
    readonly email: string
    readonly password: string
    readonly username: string
    readonly image?: File
    readonly image_name?: string
}