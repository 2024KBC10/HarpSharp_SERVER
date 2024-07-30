package com.harpsharp.infra_rds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
@AllArgsConstructor
public class UserDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 3, max = 50)
    private String password;

    @NotNull(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotNull
    private String role;

    @NotNull
    private String social_type;
}
