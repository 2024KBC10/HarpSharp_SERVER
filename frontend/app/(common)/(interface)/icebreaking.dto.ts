export interface IceBreakingDto {
    readonly id: string
    readonly username: string
    readonly createdAt: Date
    content: string
    title: string
    replies: IceBreakingCommentDto[]
    updatedAt: Date
}

export interface IceBreakingCommentDto {
    readonly id: string
    readonly username: string
    readonly createdAt: Date
    memoColor: string
    pinColor: string
    content: string
    updatedAt: Date
}

export type IceBreakingJson = {
    [id: string]: {
        username: string
        content: string
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
        memoColor: string
        pinColor: string
        createdAt: Date
        updatedAt: Date
    }
}