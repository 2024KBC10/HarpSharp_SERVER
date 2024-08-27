package com.harpsharp.album.application.outputport;

import com.harpsharp.infra_rds.entity.album.ProfileImage;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageOutputPort {
    Boolean existsByStaticUrl(String url);
    Optional<ProfileImage> findByStaticUrl(String url);
    Optional<ProfileImage> findByUUID(String uuid);
    ProfileImage save(ProfileImage profile);
}
