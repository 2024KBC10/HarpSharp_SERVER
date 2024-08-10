package com.harpsharp.infra_rds.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "todo_comments")
public class TodoComment extends BasePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_comment_id")
    private Long todoCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_post_id")
    private TodoPost todoPost;

    @Builder(toBuilder = true)
    public TodoComment(String content, User user, TodoPost todoPost) {
        this.content = content;
        this.user = user;
        this.todoPost = todoPost;
    }

    public void clearTodoPost(){
        todoPost = null;
    }
}
