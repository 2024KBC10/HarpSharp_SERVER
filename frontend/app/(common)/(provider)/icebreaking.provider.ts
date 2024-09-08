import { 
    IceBreakingCommentDto, 
    IceBreakingCommentJson, 
    IceBreakingDto,
    IceBreakingJson
} from "../(interface)"

export namespace IceBreakingProvider {
    export const toDto = (key: string, json: any): IceBreakingDto => {
        const replyKeys = Object.keys((json['comments'] as object))
        const replies: IceBreakingCommentDto[] = []
        if(replyKeys.length > 0) {
            for(let i=0; i<replyKeys.length; ++i)
                replies
                .push(
                    IceBreakingCommentProvider
                    .toDto(replyKeys[i], json['comments'][`${replyKeys[i]}`])
                )
        }

        return {
            id: key,
            title: json['title'],
            content: json['content'],
            username: json['username'],
            replies,
            likes: json['likes'],
            createdAt: json['createdAt'],
            updatedAt: json['updatedAt'],
        } satisfies IceBreakingDto
    }

    export const toJson = (question: IceBreakingDto): IceBreakingJson => {
        const { replies } = question
        const comment: IceBreakingCommentJson = {}
        if(replies.length > 0) {
            for(let i=0; i<replies.length; ++i) {
                const reply = replies[i]
                comment[`${reply.id}`] = {
                    content: reply.content,
                    username: reply.username,
                    likes: reply.likes,
                    memoColor: reply.memoColor,
                    pinColor: reply.pinColor,
                    createdAt: reply.createdAt,
                    updatedAt: reply.updatedAt,
                }
            }
        }
        
        return {
            [`${question.id}`]: {
                title: question.title,
                content: question.content,
                username: question.username,
                comment,
                likes: question.likes,
                createdAt: question.createdAt,
                updatedAt: question.updatedAt,
            }
        } satisfies IceBreakingJson
    }
}

export namespace IceBreakingCommentProvider {
    export const toDto = (key: string, json: any): IceBreakingCommentDto => ({
        id: key,
        content: json['content'],
        likes: json['likes'],
        username: json['username'],
        memoColor: json['memoColor'],
        pinColor: json['pinColor'],
        createdAt: json['createdAt'],
        updatedAt: json['updatedAt']
    } satisfies IceBreakingCommentDto)

    export const toJson = (comment: IceBreakingCommentDto) : IceBreakingCommentJson => ({
        [`${comment.id}`]: {
            username: comment.username,
            content: comment.content,
            likes: comment.likes,
            memoColor: comment.memoColor,
            pinColor: comment.pinColor,
            createdAt: comment.createdAt,
            updatedAt: comment.updatedAt,
        }
    })
}