package com.harpsharp.infra_rds.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        String password,
        String updatedUsername,
        String updatedPassword,
        String updatedEmail,
        String updatedURL) {
}
