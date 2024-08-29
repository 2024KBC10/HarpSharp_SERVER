import { 
    TaskCommentDto, 
    TaskCommentJson, 
    TaskDto,
    TaskJson,
    TaskState
} from "../../(common)/(interface)";

export namespace TaskProvider {
    export const toDto = (key: string, json: any): TaskDto => {
        const replyKeys = Object.keys((json['comments'] as object))
        const replies: TaskCommentDto[] = []
        if(replyKeys.length > 0) {
            for(let i=0; i<replyKeys.length; ++i)
                replies
                .push(
                    TaskCommentProvider
                    .toDto(json['comments'][`${replyKeys[i]}`])
                )
        }

        let status = json['status']
        switch(status) {
            case "RUNNING":
            case "STOP":
            case "DONE": status = status as TaskState
            default: status = "RUNNING"
        }

        return {
            id: key,
            username: json['username'],
            title: json['title'],
            content: json['content'],
            startAt: json['startAt'],
            endAt: json['endAt'],
            createdAt: json['createdAt'],
            updatedAt: json['updatedAt'],
            likes: parseInt(json['likes'] ?? 0),
            replies,
            status,
        } satisfies TaskDto
    }

    export const toJson = (task: TaskDto) => {
        const { replies } = task
        const comments: TaskCommentJson = {}
        if(replies.length > 0) {
            for(let i=0; i<replies.length; ++i) {
                const reply = replies[i]
                comments[`${reply.id}`] = {
                    content: reply.content,
                    username: reply.username,
                    createdAt: reply.createdAt,
                    updatedAt: reply.updatedAt,
                }
            }
        }

        return {
            [`${task.id}`]: {
                username: task.username,
                title: task.title,
                content: task.content,
                startAt: task.startAt,
                endAt: task.endAt,
                createdAt: task.createdAt,
                updatedAt: task.updatedAt,
                likes: task.likes,
                comments,
                status: task.status,
            }
        } satisfies TaskJson
    }
}

export namespace TaskCommentProvider {
    export const toDto = (json: any): TaskCommentDto => ({
        id: json['id'],
        content: json['content'],
        username: json['username'],
        createdAt: json['createdAt'],
        updatedAt: json['updatedAt']
    } satisfies TaskCommentDto)

    export const toJson = (comment: TaskCommentDto): TaskCommentJson => ({
        [`${comment.id}`]: {
            username: comment.username,
            content: comment.content,
            createdAt: comment.createdAt,
            updatedAt: comment.updatedAt,
        }
    })
}