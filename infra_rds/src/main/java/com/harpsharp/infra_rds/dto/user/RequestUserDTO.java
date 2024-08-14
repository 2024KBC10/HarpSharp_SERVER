package com.harpsharp.infra_rds.dto.user;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestUserDTO(
        @NotNull
        @Size(min = 3, max = 50)
        String username,
        String email,
        String role) {
}
