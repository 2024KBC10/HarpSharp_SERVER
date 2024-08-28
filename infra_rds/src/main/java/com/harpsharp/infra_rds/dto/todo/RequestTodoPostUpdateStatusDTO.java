package com.harpsharp.infra_rds.dto.todo;

public record RequestTodoPostUpdateStatusDTO (
        Long postId,
        TodoStatus status
){
}
