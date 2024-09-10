export interface UserDto{
    readonly id: string
    readonly email: string
    readonly createdAt: Date
    username: string
    updatedAt: Date
    socialType: string
    role: string
}

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