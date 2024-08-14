package com.harpsharp.infra_rds.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.util.TodoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "todo_posts")
public class TodoPost extends BasePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @NotNull
    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TodoStatus status;

    @OneToMany(mappedBy = "todoPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TodoComment> todoComments;

    @Column(name = "start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    @Column(name = "likes", nullable = false)
    private Long likes;

    @Builder(toBuilder = true)
    public TodoPost(String title, String content, TodoStatus status, LocalDateTime startAt, LocalDateTime endAt, User user) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
        this.user = user;
        this.likes = 0L;
        this.todoComments = new ArrayList<>();
    }
}
