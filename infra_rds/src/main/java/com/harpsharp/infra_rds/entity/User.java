package com.harpsharp.infra_rds.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 영문과 특수문자를 포함하며 8자 이상, 20자 이하이어야 합니다.")
    private String password;

    //@NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "role")
    private String role;

    //@NotNull
    @Column(name = "social_type")
    private String social_type;

    @Column(name = "profile_image")
    private String profile_image;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> comments = new ArrayList<>();

    @Builder(toBuilder = true)
    public User(String username, String password, String email, String social_type, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.social_type = social_type;
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
