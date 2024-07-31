package com.harpsharp.todo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "todo_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "username", nullable = false)
    private String username = "guest"; // Default username

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "likes", nullable = false)
    private int likes = 0;

    public enum PostStatus {
        RUNNING,
        STOP,
        DONE
    }

    @PrePersist
    protected void onCreate() {
        this.startAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = PostStatus.RUNNING;
        }
    }
}
