import { 
    TaskDto,
    UserDto, 
    UserJson 
} from "../(interface)";
import { APIManager } from "../(api)";
import { LocalStorage } from "../(storage)";
import { UserProvider } from "../(provider)";

class UserModel {
    private userData: UserDto | null = null

    public getUserData() {
        const cache = LocalStorage.get('user')
        if(cache) this.userData = JSON.parse(cache)
        return this.userData
    }

    public getLogs() {
        const cache = LocalStorage.get('logs')
        if(cache) return JSON.parse(cache)
        return []
    }

    public setFocusTask(task: TaskDto): boolean {
        const cache = LocalStorage.get("focus")
        if(cache) return false

        LocalStorage.set("focus", JSON.stringify(task))
        return true
    }

    public getFocusTask(): TaskDto | null {
        const cache = LocalStorage.get("focus")
        if(cache) return JSON.parse(cache) as TaskDto
        return null
    }

    public removeFocusTask() { LocalStorage.remove("focus") }

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
                LocalStorage.set("user", JSON.stringify(this.userData))
                LocalStorage.set("logs", "[]")
            }
            else throw new Error("로그인 실패: 응답 헤더에 토큰이 없습니다.")
            return true
        }

        return false
    }

    public logout() {
        this.userData = null
        LocalStorage.clear()
    }
    public async signUp(args: IUserSignUpArgs): Promise<boolean> {
        const response = await APIManager.post({
            route: "/join",
            body: {
                email: args.email,
                username: args.username,
                password: args.password,
                position: args.position,
            }
        })

        if("message" in response && response.code === 201) return true
        return false
    }

    public async updateTask(task: TaskDto) {
        const token = LocalStorage.get("token")
        const response = await APIManager.patch({
            route: "/todo/posts",
            body: {
                postId: task.id,
                username: this.userData?.username,
                title: task.title,
                content: task.content,
                status: task.status,
                startAt: task.startAt,
                endAt: task.endAt
            },
            headers: { authorization: `${token}`}
        })

        if("data" in response && response.code === 200) return true
        return false
    }

    public updateTasks(
        tasks: TaskDto[], 
        message: string,
    ) {
        if(this.userData) {
            this.addLog(message)
            this.userData.myTodos = tasks
            LocalStorage.set('user', JSON.stringify(this.userData))
        }
    }

    public addLog(content: string) {
        const logs: IActivityLogs[] = JSON.parse(LocalStorage.get("logs") ?? "[]")
        logs.push({
            content,
            createdAt: new Date()
        })
        LocalStorage.set('logs', JSON.stringify(logs))
    }

    public isSubscribeComment(commentId: string): boolean {
        return !!(this.userData!.likeComments.find(id => id === parseInt(commentId)))
    }

    public async deSubscribeComment(commentId: string, writer: string) {
        const token = LocalStorage.get("token")
        const response = await APIManager.post({
            route: "/board/comments/likes",
            body: {
                username: this.userData!.username,
                commentId,
            },
            headers: { authorization: `${token}` }
        })

        if("data" in response && response.code === 201) {
            this.addLog(`${writer}님의 답변에 관심을 표시했습니다.`)
            this.userData!.likeComments = this.userData!.likeComments.filter(id => id !== parseInt(commentId))
            LocalStorage.set('user', JSON.stringify(this.userData))
            const data = response.data as { username: string, postId: number | string, likes: number }
            return data.likes
        }
        throw new Error("요청을 처리하는데 실패했습니다.")
    }

    public async subscribeComment(commentId: string, writer: string) {
        const token = LocalStorage.get("token")
        const response = await APIManager.post({
            route: "/board/comments/likes",
            body: {
                username: this.userData!.username,
                commentId,
            },
            headers: { authorization: `${token}` }
        })

        if("data" in response && response.code === 201) {
            this.addLog(`${writer}님의 답변에 관심을 표시했습니다.`)
            this.userData!.likeComments.push(parseInt(commentId))
            LocalStorage.set('user', JSON.stringify(this.userData))
            const data = response.data as { username: string, postId: number | string, likes: number }
            return data.likes
        }
        throw new Error("요청을 처리하는데 실패했습니다.")
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
    readonly position: Position
    readonly username: string
    readonly image?: File
    readonly image_name?: string
}

export interface IActivityLogs {
    content: string
    createdAt: Date
}

export type Position = "CLOUD" | "FULLSTACK" | "AI"