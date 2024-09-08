export interface IceBreakingDto {
    readonly id: string
    readonly username: string
    readonly createdAt: Date
    content: string
    title: string
    replies: IceBreakingCommentDto[]
    likes: number
    updatedAt: Date
}

export interface IceBreakingCommentDto {
    readonly id: string
    readonly username: string
    readonly createdAt: Date
    memoColor: string
    pinColor: string
    content: string
    likes: number
    updatedAt: Date
}

export type IceBreakingJson = {
    [id: string]: {
        username: string
        content: string
        likes: number
        title: string
        comment: IceBreakingCommentJson
        createdAt: Date
        updatedAt: Date
    }
}

export type IceBreakingCommentJson = {
    [id: string]: {
        username: string
        content: string
        likes: number
        memoColor: string
        pinColor: string
        createdAt: Date
        updatedAt: Date
    }
}