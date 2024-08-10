package com.harpsharp.infra_rds.dto.user;

import jakarta.validation.constraints.Pattern;

public record LoginDTO(
        String username,
                       @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 영문, 특수문자, 숫자를 포함하는 8자 이상, 20자 이하이어야 합니다.")
                       String password)
{}
