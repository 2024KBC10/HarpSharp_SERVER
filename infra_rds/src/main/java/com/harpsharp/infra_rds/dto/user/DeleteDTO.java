package com.harpsharp.infra_rds.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.*;

public record DeleteDTO(
        String password
) {
}
