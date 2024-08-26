package com.harpsharp.infra_rds.entity.board;

import com.harpsharp.infra_rds.entity.BasePost;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "memo_color")
    String memoColor;

    @NotNull
    @Column(name = "pin_color")
    String pinColor;


    @Builder(toBuilder = true)
    public Comment(User user, String content, Post post, String memoColor, String pinColor){
        this.setContent(content);
        this.setUser(user);
        this.post = post;
        this.memoColor = memoColor;
        this.pinColor = pinColor;
        post.addComment(this);
    }

    public String getUsername(){
        return getUser().getUsername();
    }

    public void clearPost(){
        post = null;
    }

    @PrePersist
    public void prePersist() {
        if (this.memoColor == null) {
            this.memoColor = "yellow";
        }
        if (this.pinColor == null) {
            this.pinColor = "brown";
        }
    }
}
