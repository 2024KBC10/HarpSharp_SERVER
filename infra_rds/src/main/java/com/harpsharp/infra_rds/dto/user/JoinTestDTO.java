package com.harpsharp.infra_rds.dto.user;


public record JoinTestDTO(
        String username,
        String password,
        String email,
        String url)
{}