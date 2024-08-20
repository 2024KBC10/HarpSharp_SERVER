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

/*"2": {
      "username": "admin",
      "email": "admin@gmail.com",
      "createdAt": "2024-08-12 02:38:18",
      "updatedAt": "2024-08-12 02:38:18",
      "socialType": "harp",
      "role": "ROLE_USER",
      "posts": {},
      "comments": {},
      "todoPosts": {},
      "todoComments": {}
    }*/