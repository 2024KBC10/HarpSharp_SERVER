package com.harpsharp.infra_rds.entity.board;

import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

@Getter
@Entity
@DynamicUpdate
@Table(name = "posts")
@DynamicInsert
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BasePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @NotNull
    @Column(name = "title")
    String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public String getUsername() {
        return getUser().getUsername();
    }

    public void addComment   (Comment comment) {
         comments.add(comment);
    }
    public void removeComment(Comment comment) { comments.remove(comment); }

    public void addLike   (PostLike like)  { postLikes.add(like);       }
    public void removeLike(PostLike like)  { postLikes.remove(like);    }

    @NotNull
    @Column(name = "likes_count")
    Long likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    @PrePersist
    protected void onCreate() {
        if(this.postLikes == null){
            this.postLikes = new ArrayList<>();
        }

        if(this.likes == null){
            likes = 0L;
        }

        if(this.comments == null){
            this.comments = new ArrayList<>();
        }
    }

    @PreUpdate
    private void updateLikesCount() {
        if(this.postLikes == null){
            this.postLikes = new ArrayList<>();
        }

        this.likes = postLikes.stream().count();
    }
}
