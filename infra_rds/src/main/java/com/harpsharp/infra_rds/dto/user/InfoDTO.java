package com.harpsharp.infra_rds.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class InfoDTO {
    private final String username;
    private final String role;
}
