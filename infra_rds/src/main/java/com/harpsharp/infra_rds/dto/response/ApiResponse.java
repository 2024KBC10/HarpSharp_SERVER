package com.harpsharp.infra_rds.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record ApiResponse (String code, String message)
{}
