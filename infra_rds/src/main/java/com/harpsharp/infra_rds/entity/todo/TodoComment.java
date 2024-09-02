package com.harpsharp.infra_rds.entity.todo;

import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@DynamicUpdate
@Table(name = "todo_comments")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoComment extends BasePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_comment_id")
    private Long todoCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_post_id")
    private TodoPost todoPost;

    public void clearTodoPost(){
        todoPost = null;
    }
}
