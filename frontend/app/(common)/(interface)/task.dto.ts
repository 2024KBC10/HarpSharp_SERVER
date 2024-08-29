export interface TaskDto {
    readonly id: string
    readonly startAt: Date
    readonly createdAt: Date
    readonly username: string
    replies: TaskCommentDto[]
    title: string
    status: TaskState
    content: string
    updatedAt: Date
    endAt: Date
    likes: number
}

export interface TaskCommentDto {
    readonly id: string
    readonly username: string
    content: string
    createdAt: Date
    updatedAt: Date
}

export type TaskJson = {
    [id: string]: {
        comments: TaskCommentJson
        content: string
        title: string
        startAt: Date
        createdAt: Date
        username: string
        status: TaskState
        updatedAt: Date
        endAt: Date
        likes: number
    }
}

export type TaskCommentJson = {
    [id: string]: {
        username: string
        content: string
        createdAt: Date
        updatedAt: Date
    }
}

export type TaskState = "RUNNING" | "STOP" | "DONE"