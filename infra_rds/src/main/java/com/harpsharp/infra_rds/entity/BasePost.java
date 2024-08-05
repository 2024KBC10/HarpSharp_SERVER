package com.harpsharp.infra_rds.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BasePost {
    @NotNull
    @Column(name = "content")
    protected String content;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
