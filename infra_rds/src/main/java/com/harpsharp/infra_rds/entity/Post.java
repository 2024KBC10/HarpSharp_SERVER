package com.harpsharp.infra_rds.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@Table(name = "posts")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BasePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @NotNull
    @Column(name = "title")
    String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    public String getUsername() {
        return getUser().getUsername();
    }

    public Long getUserId(){
        return getUser().getUserId();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Builder(toBuilder = true)
    public Post(Long postId, User user, String content, String title, LocalDateTime createdAt){
        this.postId = postId;
        this.setUser(user);
        this.setContent(content);
        this.title = title;
        this.createdAt = createdAt;
        this.comments = new ArrayList<>();
    }
}
