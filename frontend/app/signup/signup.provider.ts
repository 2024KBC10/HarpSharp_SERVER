import { use, useInsertionEffect } from "react";
import { UserDto, UserJson } from "./signup.dto";

export namespace UserProvider{
    export const userinfoDto = (key: string, json: any): UserDto =>({
        id: key,
        email: json['email'],
        createdAt: json['createdAt'],
        username: json['username'],
        updatedAt: json['updatedAt'],
        socialType: json['socialType'],
        role: json['role']
    } satisfies UserDto)

    export const toJson = (userinfoDto: UserDto) : UserJson => ({
        [`${userinfoDto.id}`]:{
            email: userinfoDto.email,
            createdAt: userinfoDto.createdAt,
            username: userinfoDto.username,
            updatedAt: userinfoDto.updatedAt,
            socialType: userinfoDto.socialType,
            role: userinfoDto.role
        }
    })
}

/*readonly id: string
    readonly email: string
    readonly createdAt: Date
    username: string
    updatedAt: Date
    socialType: string
    role: string
    
    export type UserJson = {
    [id: string]:{
        email: string
        createdAt: Date
        username: string
        updatedAt: Date
        socialType: string
        role: string
    }
}

    */