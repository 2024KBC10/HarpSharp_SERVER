package com.harpsharp.infra_rds.dto.user;

import jakarta.validation.constraints.Pattern;

public record LoginDTO(
        String username,
        String password)
{}
