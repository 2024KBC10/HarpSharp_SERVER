const BASE_URL = process.env.NEXT_PUBLIC_PROXY_URL

interface RequestArgs {
    readonly route: string
    readonly headers?: HeadersInit
    readonly body?: object
}

export namespace APIManager {
    export const get = async <T extends unknown>(
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            const response = await fetch(BASE_URL, {
                headers: {
                    ...args.headers,
                    location: args.route,
                },
                method: "GET",
            })
            .then(result => result.json())
            
            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                const success = response as SuccessResponse<T>
                if(success.code === 200) return typeof success.data === "undefined" ? true : success.data as T
                return null
            }        
        } catch(e) { throw e }
    }
    export const post = async (
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")

            const response = await fetch(BASE_URL, {
                body: args.body ? JSON.stringify(args.body) : undefined,
                method: "POST",
                headers: {
                    ...args.headers,
                    location: args.route,
                },           

            })
            .then(result => result.json())
            
            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                if(response.code === 201) return true
                return false
            }  
        } catch(e) { throw e }
    }
    export const patch = async (
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            
            const response = await fetch(BASE_URL, {
                body: args.body ? JSON.stringify(args.body) : undefined,
                method: "PATCH",
                headers: {
                    ...args.headers,
                    location: args.route,
                },
            })
            .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                if(response.code === 200) return true
                return false
            }  
        } catch(e) { throw e }
    }
    export const del = async (
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            
            const response = await fetch(BASE_URL, {
                body: args.body ? JSON.stringify(args.body) : undefined,
                method: "DELETE",
                headers: {
                    ...args.headers,
                    location: args.route,
                },
            })
            .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                if(response.code === 200) return true
                return false
            }        
        } catch(e) { throw e }
    }

    const _handleFailure = (response: FailureReponse) => {
        switch(response.code) {
            case 401:
                return "요청하신 데이터를 찾을 수 없습니다."
            case 409:
                return "이미 등록 된 정보입니다."
            default:{
                console.log(`[처리 할 수 없는 상태 코드]: ${response.code}\n[오류 내용]: ${response.error}`)
                throw new Error("<p>요청 처리에 실패했습니다.<br/>인터넷 통신환경을 확인해주세요.</p>")
            }
        } 
    }
}

type ResponseStatus = 
| 200 // Get, Patch, Put, Delete
| 201 // Post
| 401 // 없거나 유효하지 않은 데이터 조회
| 409 // 이미 존재하는 데이터 추가 요청 같은 코드

interface Response {
    readonly timestamp: Date
    readonly code: ResponseStatus
}

interface SuccessResponse<T> extends Response {
    readonly message: string // 결과 상태 메세지
    readonly details: string // 결과 상태 상세 메시지
    readonly data?: T
}

interface FailureReponse extends Response {
    readonly error: string // 에러 내용
    readonly path: string // 요청 라우트
}


// Request, Response Dto로 나뉨
// 토큰은 헤더에 담겨서 오고, 프론트도 헤더에 담아서 보내야 함 
// // Authorization
