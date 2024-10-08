package com.harpsharp.infra_rds.dto.todo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoStatus {
    RUNNING("RUNNING"),
    STOP("STOP"),
    DONE("DONE");

    private final String status;
}
