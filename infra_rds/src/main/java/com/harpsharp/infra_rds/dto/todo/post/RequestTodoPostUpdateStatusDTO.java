package com.harpsharp.infra_rds.dto.todo.post;

import com.harpsharp.infra_rds.dto.todo.TodoStatus;

public record RequestTodoPostUpdateStatusDTO (
        Long postId,
        TodoStatus status
){
}
