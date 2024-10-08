import { IceBreakingDto, IceBreakingJson } from "./icebreaking.dto"
import { TaskDto, TaskJson } from "./task.dto"

export interface UserDto{
    readonly id: string
    readonly email: string
    readonly createdAt: Date
    myTodos: TaskDto[]
    myQuestions: IceBreakingDto[]
    likeComments: number[]
    position: string
    username: string
    updatedAt: Date
    socialType: string
    role: string
}

export type UserJson = {
    [id: string]:{
        email: string
        createdAt: Date
        myTodos: TaskJson[]
        myQuestions: IceBreakingJson[]
        likeComments: number[]
        username: string
        position: string
        updatedAt: Date
        socialType: string
        role: string
    }
}
