package com.harpsharp.infra_rds.repository;


import com.harpsharp.infra_rds.entity.album.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageJpaRepository extends JpaRepository<ProfileImage, Long> {
    Boolean existsByUrl(String url);
    Optional<ProfileImage> findByProfileId(Long profileId);
    Optional<ProfileImage> findByUuid(String uuid);
    Optional<ProfileImage> findByUrl(String url);
}
