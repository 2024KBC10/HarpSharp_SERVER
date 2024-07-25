package com.harpsharp.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateDTO {
    @NotNull
    @Size(min = 3, max = 50)
    private String username;
    private String password;
    private String email;
}
