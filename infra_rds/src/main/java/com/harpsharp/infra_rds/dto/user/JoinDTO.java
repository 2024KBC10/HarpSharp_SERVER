package com.harpsharp.infra_rds.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;


public record JoinDTO(
        String username,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 영문과 특수문자를 포함하며 8자 이상, 20자 이하이어야 합니다.")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        String email)
{}
