package com.harpsharp.infra_rds.entity.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.todo.TodoStatus;
import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "todo_posts")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "todoPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoComment> todoComments;

    @Column(name = "start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public void addComment(TodoComment comment) {
        todoComments.add(comment);
    }

    public void removeComment(TodoComment comment){
        todoComments.remove(comment);
    }

    @PrePersist
    protected void onCreate() {
        if(this.todoComments == null){
            todoComments = new ArrayList<>();
        }
    }
}
