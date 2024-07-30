package com.harpsharp.infra_rds.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@Data
@RequiredArgsConstructor
public class InfoDTO {
    private final String username;
    private final String role;
}