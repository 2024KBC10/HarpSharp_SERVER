package com.harpsharp.infra_rds.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public record JoinDTO(
        String username,
        String password,
        String email,
        String url)
{}
