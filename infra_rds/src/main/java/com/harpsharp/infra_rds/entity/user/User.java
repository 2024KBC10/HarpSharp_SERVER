package com.harpsharp.infra_rds.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.user.Position;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.todo.TodoComment;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "position")
    private Position position;

    @NotNull
    @Column(name = "role")
    private String role;

    @Column(name = "social_type")
    private String socialType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_url", unique = true, referencedColumnName = "url")
    private ProfileImage profileImage;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<TodoPost> todoPosts;

    @OneToMany(mappedBy = "user")
    private List<TodoComment> todoComments;

    @Builder(toBuilder = true)
    public User(String username, String password, String email, Position position, String socialType, ProfileImage profileImage, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.position = position;
        this.role = role;
        this.socialType = socialType;
        this.profileImage = profileImage;
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.todoPosts = new ArrayList<>();
        this.todoComments = new ArrayList<>();
    }

    public User updateUser(User updatedUser){
        if(updatedUser.username != null){
            this.username = updatedUser.getUsername();
        }
        if(updatedUser.password != null){
            this.password = updatedUser.getPassword();
        }
        if(updatedUser.email != null){
            this.email = updatedUser.getEmail();
        }

        return this;
    }
}
