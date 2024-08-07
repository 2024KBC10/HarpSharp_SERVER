package com.harpsharp.infra_rds.dto.response;

public record ResponseWithData<T>(String code,
                                  String message,
                                  T data) { }

/*
{
    time: 응답 시간(LocalTime),
    code: HttpStatus,
    message: INVALID_TOKEN
    details: 한글 상세 사안,
    data: DTO(post, comment, user...)
}
 */

/*
{
    code: ,
    message:
}
 */