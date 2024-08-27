package com.harpsharp.infra_rds.entity.album;
import com.harpsharp.infra_rds.dto.image.ImageType;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "profile_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileImage extends Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Builder(toBuilder = true)
    public ProfileImage(String url, String uuid) {
        this.url = url;
        this.uuid = uuid;
    }

    @PrePersist
    protected void onCreate() {
        this.type = ImageType.PROFILE;
    }
}
