package com.harpsharp.infra_rds.entity.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harpsharp.infra_rds.dto.image.ImageType;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Image {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected ImageType type;

    @NotNull
    @Column(unique = true)
    protected String url;

    @NotNull
    protected String uuid;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime updatedAt;
}
