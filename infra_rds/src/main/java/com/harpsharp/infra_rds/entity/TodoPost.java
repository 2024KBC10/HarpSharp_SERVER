package com.harpsharp.infra_rds.entity;

import com.harpsharp.infra_rds.util.TodoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity(name = "TodoPost")
@Setter
@DynamicUpdate
public class TodoPost extends BasePost{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @NotNull
    @Column(name = "title")
    String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TodoStatus status;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "likes", nullable = false)
    private Long likes = 0L;
}
