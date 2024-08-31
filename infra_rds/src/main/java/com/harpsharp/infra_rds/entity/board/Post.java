package com.harpsharp.infra_rds.entity.board;

import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

    public Long incLikes()  {   likes++; return likes;  };
    public Long decLikes()  {   likes = max(0, likes-1); return likes;  };

    public String getUsername() {
        return getUser().getUsername();
    }
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @NotNull
    @Column(name = "likes_count")
    private Long likes;

    @Builder(toBuilder = true)
    public Post(User user, String content, String title, Long likes){
        this.setUser(user);
        this.setContent(content);
        this.title = title;
        this.comments = new ArrayList<>();
        this.likes = likes;
    }

    @PrePersist
    protected void onCreate() {
        if(this.likes == null){
            this.likes = 0L;
        }
    }
}
