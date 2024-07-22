package com.harpsharp.auth.repository;

import com.harpsharp.auth.entity.RefreshEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<RefreshEntity, String> {
    @Transactional
    boolean existsById(String access);

    @Transactional
    void deleteById(String refresh);

    @Transactional
    Optional<RefreshEntity> findById(String access);
}
