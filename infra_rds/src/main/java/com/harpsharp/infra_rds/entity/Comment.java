package com.harpsharp.infra_rds.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BasePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder(toBuilder = true)
    public Comment(User user, String content, Post post){
        this.setContent(content);
        this.setUser(user);
        this.post = post;
    }

    public String getUsername(){
        return getUser().getUsername();
    }
}
