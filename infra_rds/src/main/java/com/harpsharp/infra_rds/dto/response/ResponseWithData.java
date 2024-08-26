package com.harpsharp.infra_rds.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseWithData<T>(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime timeStamp,
        Integer code,
        String message,
        String details,
        T data) { }

/*
{
    time: 응답 시간(LocalTime),
    code: HttpStatus,
    message: 압축한 안내문 (etc. INVALID_TOKEN)
    details: 상세 설명(한글),
    data: dto(post, comment, user...)
}
 */

/*
{
    code: ,
    message:
}
 */