package com.harpsharp.infra_rds.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Component
@RequiredArgsConstructor
public class ResponseUtils {
    private final ObjectMapper objectMapper;

    public <T> void writeResponseEntityWithData(HttpServletResponse response, ResponseWithData<T> responseEntity) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(responseEntity);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public void writeResponseEntity(HttpServletResponse response, ResponseEntity<ApiResponse> responseEntity) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(responseEntity);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public void writeResponseEntity(HttpServletResponse response, Integer status, String message, String details) throws IOException {
        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        status,
                        message,
                        details);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public void writeUnauthorizedEntity(HttpServletResponse response, Integer status, String message, String details) throws IOException {
        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        status,
                        message,
                        details);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public void writeBadRequestEntity(HttpServletResponse response, Integer status, String message, String details) throws IOException {
        ApiResponse apiResponse =
                new ApiResponse(
                        LocalDateTime.now(),
                        status,
                        message,
                        details);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public <T> void writeResponseEntityWithData(HttpServletResponse response, Integer status, String message, String details, T data) throws IOException {

        ResponseWithData<T> apiResponse =
                new ResponseWithData<T>(
                        LocalDateTime.now(),
                        status,
                        message,
                        details,
                        data);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException, JsonMappingException {
        return objectMapper.readValue(content, valueType);
    }
}
