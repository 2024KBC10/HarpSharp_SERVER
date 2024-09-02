package com.harpsharp.infra_rds.entity.board;

import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@DynamicUpdate
@Entity
@DynamicInsert
@Table(name = "comments")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BasePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Version
    Long version;

    @NotNull
    @Column(name = "memo_color")
    String memoColor;

    @NotNull
    @Column(name = "pin_color")
    String pinColor;

    @NotNull
    @Column(name = "likes_count")
    Long likes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes;

    public void addLike   (CommentLike like)  {
        isValid();
        commentLikes.add(like);
        likes = commentLikes.stream().count();
    }

    public void isValid(){
        if(commentLikes == null){
            commentLikes = new ArrayList<>();
        }
    }

    public void removeLike(CommentLike like)  {
        commentLikes.remove(like);
        likes = commentLikes.stream().count();
    }

    public String getUsername(){
        return getUser().getUsername();
    }

    public void clearPost(){
        post = null;
    }

    @PrePersist
    public void onCreate() {
        if (this.memoColor == null) {
            this.memoColor = "yellow";
        }
        if (this.pinColor == null) {
            this.pinColor = "brown";
        }

        if(this.commentLikes == null){
            this.commentLikes = new ArrayList<>();
        }

        if(this.likes == null){
            likes = 0L;
        }
    }
}
