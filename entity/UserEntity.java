package com.harpsharp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
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

    @Builder(toBuilder = true)
    public UserEntity(String username, String password, String email, String social_type, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.social_type = social_type;
    }

    public void updateUser(UserEntity updatedUser){
        if(updatedUser.username != null){
            this.username = updatedUser.getUsername();
        }
        if(updatedUser.password != null){
            this.password = updatedUser.getPassword();
        }
        if(updatedUser.email != null){
            this.email = updatedUser.getEmail();
        }
    }
}
