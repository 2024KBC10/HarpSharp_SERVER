import { 
    IceBreakingDto,
    TaskDto, 
    UserDto, 
    UserJson 
} from "../(interface)";
import { IceBreakingProvider } from "./icebreaking.provider";
import { TaskProvider } from "./task.provider";

export namespace UserProvider{
    export const userinfoDto = (key: string, json: any): UserDto => {
        const todos: TaskDto[] = []
        const todosKeys = Object.keys(json['todoPosts'])
        if(todosKeys.length > 0) {
            for(let i=0; i<todosKeys.length; ++i) {
                const key = todosKeys[i]
                todos
                .push(
                    TaskProvider
                    .toDto(key, json['todoPosts'][key])
                )
            }
        }

        const icebreakings: IceBreakingDto[] = []
        const icebreakingsKeys = Object.keys(json['posts'])
        if(icebreakingsKeys.length > 0) {
            for(let i=0; i<icebreakingsKeys.length; ++i) {
                const key = icebreakingsKeys[i]
                icebreakings
                .push(
                    IceBreakingProvider
                    .toDto(key, json['posts'][key])
                )
            }
        }

        return {
            id: key,
            email: json['email'],
            createdAt: json['createdAt'],
            myQuestions: icebreakings,
            myTodos: todos,
            likeComments: json['likeComments'] as number[],
            position: json['position'],
            username: json['username'],
            updatedAt: json['updatedAt'],
            socialType: json['socialType'],
            role: json['role']
        } satisfies UserDto
    }

    export const toJson = (userinfoDto: UserDto) : UserJson => ({
        [`${userinfoDto.id}`]:{
            email: userinfoDto.email,
            createdAt: userinfoDto.createdAt,
            myQuestions: userinfoDto.myQuestions.map(IceBreakingProvider.toJson),
            myTodos: userinfoDto.myTodos.map(TaskProvider.toJson),
            likeComments: userinfoDto.likeComments,
            position: userinfoDto.position,
            username: userinfoDto.username,
            updatedAt: userinfoDto.updatedAt,
            socialType: userinfoDto.socialType,
            role: userinfoDto.role
        }
    })
}