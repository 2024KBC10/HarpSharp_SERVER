import { NextRequest, NextResponse } from 'next/server'

        const BASE_URL = process.env.NEXT_PUBLIC_PROXY_URL
        const BASE_HEADERS = {
            "Accept": "application/json;charset=UTF-8;",
            "Content-Type": "application/json;charset=UTF-8;"
        }

        export const GET = async (request: NextRequest) => {
            const route = request.headers.get('location')
            try {
                const signal = _onRequestTimeLimit()
                const response = await fetch(`${BASE_URL}${route}`, {
                    headers: {
                        ...BASE_HEADERS,
                    },
                    method: "GET",
                    mode: "cors",
                    signal,
                })
                    .then(res => res.json())

                // 통신 성공 하면 클라이언트에서 알리기
                return new NextResponse(
            JSON.stringify(response),
            {
                status: 200,
            }
        )
      
    } catch(e) {
      return new NextResponse(
        "server error", {
      	status: 500
      }) 
    }
}

export const POST = async (request: NextRequest) => {
    const route = request.headers.get('location')
    const body = await request
    .json()
    .then(JSON.stringify)

    try {
        console.log(BASE_URL);
        const signal = _onRequestTimeLimit()
        const response = await fetch(`${BASE_URL}${route}`, {
            headers: {
                ...BASE_HEADERS,
            },
            body,
            method: "POST",
            mode: "cors",
            signal,
        })
        .then(res => res.json())
        console.log(response);
        // 통신 성공 하면 클라이언트에서 알리기
        return new NextResponse(
            JSON.stringify(response),
            {
                status: 201,
            }
        )
      
    } catch(e) {
      return new NextResponse(
        "server error", {
      	status: 500
      }) 
    }
}

export const PATCH = async (request: NextRequest) => {
    const route = request.headers.get('location')
    const body = await request
    .json()
    .then(JSON.stringify)

    try {
        const signal = _onRequestTimeLimit()
        const response = await fetch(`${BASE_URL}${route}`, {
            headers: {
                ...BASE_HEADERS,
            },
            body,
            method: "PATCH",
            mode: "cors",
            signal,
        })
        .then(res => res.json())

        // 통신 성공 하면 클라이언트에서 알리기
        return new NextResponse(
            JSON.stringify(response),
            {
                status: 200,
            }
        )
      
    } catch(e) {
      return new NextResponse(
        "server error", {
      	status: 500
      }) 
    }
}

export const DELETE = async (request: NextRequest) => {
    const route = request.headers.get('location')
     const body = await request
    .json()
    .then(JSON.stringify)

    try {
        const signal = _onRequestTimeLimit()
        const response = await fetch(`${BASE_URL}${route}`, {
            headers: {
                ...BASE_HEADERS,
            },
            body,
            method: "DELETE",
            mode: "cors",
            signal,
        })
        .then(res => res.json())

        // 통신 성공 하면 클라이언트에서 알리기
        return new NextResponse(
            JSON.stringify(response),
            {
                status: 200,
            }
        )
      
    } catch(e) {
      return new NextResponse(
        "server error", {
      	status: 500
      }) 
    }
}

/**
     * 설정 된 시간이 만료되면 http 요청을 중단합니다.
     * @returns 
     */
const _onRequestTimeLimit = () => {
    const abortController = new AbortController()
    const signal_ttl = parseInt(process.env.NEXT_PUBLIC_SIGNAL_TTL ?? "5000")
    setTimeout(() => abortController.abort(), signal_ttl)

    return abortController.signal
}