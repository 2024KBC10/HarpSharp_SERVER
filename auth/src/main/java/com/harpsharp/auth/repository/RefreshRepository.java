package com.harpsharp.auth.repository;

import com.harpsharp.auth.entity.RefreshEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<RefreshEntity, Long> {
    @Transactional
    boolean existsById(Long userId);

    @Transactional
    void deleteById(Long userId);

    @Transactional
    Optional<RefreshEntity> findById(Long userId);
}
