package com.harpsharp.album.framework.exceptions;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//AwsServiceException, SdkClientException, S3Exception
    @ExceptionHandler(AwsServiceException.class)
    private ResponseEntity<ApiResponse> handleAwsServiceException(AwsServiceException e) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                e.statusCode(),
                e.getMessage(),
                "AWS에서 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @ExceptionHandler(SdkClientException.class)
    private ResponseEntity<ApiResponse> handleSdkClientExceptionException(SdkClientException e) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                e.getMessage(),
                "SDK 클라이언트에서 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
    //S3Exception

    @ExceptionHandler(S3Exception.class)
    private ResponseEntity<ApiResponse> handleS3ExceptionException(S3Exception e) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                e.statusCode(),
                e.getMessage(),
                "S3에서 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }


    @ExceptionHandler(MalformedURLException.class)
    private ResponseEntity<ApiResponse> handleMalformedURLException(MalformedURLException e) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "유효하지 않은 URL입니다.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "유효하지 않은 파일 확장자 입니다."
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

}