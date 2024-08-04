package com.harpsharp.infra_rds.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateUserDTO {
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 영문과 특수문자를 포함하며 8자 이상, 20자 이하이어야 합니다.")
    private String password;
    private String email;
}
