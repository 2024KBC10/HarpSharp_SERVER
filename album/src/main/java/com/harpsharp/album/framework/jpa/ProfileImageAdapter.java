package com.harpsharp.album.framework.jpa;

import com.harpsharp.album.application.outputport.ProfileImageOutputPort;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.repository.ProfileImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileImageAdapter implements ProfileImageOutputPort {
    private final ProfileImageJpaRepository profileImageJpaRepository;


    @Override
    public Boolean existsByStaticUrl(String url) {
        return profileImageJpaRepository.existsByUrl(url);
    }

    @Override
    public Optional<ProfileImage> findByStaticUrl(String url) {
        return profileImageJpaRepository.findByUrl(url);
    }

    @Override
    public Optional<ProfileImage> findByUUID(String uuid) {
        return profileImageJpaRepository.findByUuid(uuid);
    }

    @Override
    public ProfileImage save(ProfileImage profile) {
        return profileImageJpaRepository.save(profile);
    }
}
