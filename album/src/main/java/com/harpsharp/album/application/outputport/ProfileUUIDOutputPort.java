package com.harpsharp.album.application.outputport;

import com.harpsharp.infra_rds.entity.album.ProfileUUID;
import java.util.Optional;

public interface ProfileUUIDOutputPort {
    void save(ProfileUUID profileUUID);
    Optional<ProfileUUID> findByUsername(String username);
}
