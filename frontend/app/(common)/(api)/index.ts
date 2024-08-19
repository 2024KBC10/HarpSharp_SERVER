const BASE_URL = process.env.NEXT_PUBLIC_API_URL

interface RequestArgs {
    readonly route: string
    readonly headers?: HeadersInit
    readonly body?: object
}

const BASE_HEADERS = {
    "Accept": "application/json;charset=UTF-8;",
    "Content-Type": "application/json;charset=UTF-8"
}

export namespace APIManager {
    export const get = async <T extends unknown>(
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            const signal = _onRequestTimeLimit();
            const response = await fetch(BASE_URL, {
                headers: {
                    ...args.headers,
                    location: args.route,
                },
                method: "GET",
                mode: "cors",
                signal,
            })
                .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                const success = response as SuccessResponse<T>
                if(success.code === 200) return typeof success.data === "undefined" ? true : success.data as T
                return _handleFailure(response as FailureReponse)
            }
        } catch(e) { throw e }
    }
    export const post = async <T extends unknown>(
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            const url = `${BASE_URL}/api/v1/${args.route}`;
            console.log(url);
            console.log(args.headers);
            const signal = _onRequestTimeLimit()
            const response = await fetch(url, {
                body: args.body ? JSON.stringify(args.body) : undefined,
                headers: {
                    ...BASE_HEADERS,
                },
                method: "POST",
                mode: "cors",
                signal,
            })
                .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                const success = response as SuccessResponse<T>
                if(success.code === 201) return success
                return _handleFailure(response as FailureReponse)
            }
        } catch(e) { throw e }
    }
    export const patch = async <T extends unknown>(
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            const signal = _onRequestTimeLimit()
            const response = await fetch(BASE_URL, {
                headers: {
                    ...BASE_HEADERS,
                    location: args.route,
                },
                method: "PATCH",
                mode: "cors",
                signal,
            })
                .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                const success = response as SuccessResponse<T>
                if(success.code === 200) return success
                return _handleFailure(response as FailureReponse)
            }
        } catch(e) { throw e }
    }
    export const del = async <T extends unknown> (
        args: RequestArgs,
    ) => {
        try {
            if(typeof BASE_URL === "undefined") throw new Error("<p>요청에 실패했습니다.<br/>브라우저를 종료하고 재 접속 후, 다시시도 해주세요.</p>")
            const signal = _onRequestTimeLimit()
            const response = await fetch(BASE_URL, {
                headers: {
                    ...BASE_HEADERS,
                    location: args.route,
                },
                method: "DELETE",
                mode: "cors",
                signal,
            })
                .then(result => result.json())

            if("error" in response) return _handleFailure(response as FailureReponse)
            else {
                const success = response as SuccessResponse<T>
                if(success.code === 200) return success
                return _handleFailure(response as FailureReponse)
            }
        } catch(e) { throw e }
    }

    const _handleFailure = (response: FailureReponse) => {
        switch(response.code) {
            case 401:
            case 409:
                return response
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
    readonly authorization?: string // JWT 토큰
    readonly data?: T
}

interface FailureReponse extends Response {
    readonly error: string // 에러 내용
    readonly path: string // 요청 라우트
}

const _onRequestTimeLimit = () => {
    const abortController = new AbortController()
    const signal_ttl = parseInt(process.env.NEXT_PUBLIC_SIGNAL_TTL ?? "5000")
    setTimeout(() => abortController.abort(), signal_ttl)

    return abortController.signal
}


// Request, Response Dto로 나뉨
// 토큰은 헤더에 담겨서 오고, 프론트도 헤더에 담아서 보내야 함
// // Authorization
