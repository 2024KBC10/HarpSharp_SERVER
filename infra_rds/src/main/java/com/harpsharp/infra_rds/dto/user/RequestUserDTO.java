package com.harpsharp.infra_rds.dto.user;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestUserDTO(
        @NotNull
        @Size(min = 3, max = 50)
        String username,
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "유효하지 않은 이메일 형식 입니다.")
        String email,
        String role,
        String socialType) {
}
