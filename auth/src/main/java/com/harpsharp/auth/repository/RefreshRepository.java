package com.harpsharp.auth.repository;

import com.harpsharp.auth.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshRepository extends CrudRepository<RefreshToken, Long> {
    @Transactional
    boolean existsById(Long userId);

    @Transactional
    void deleteById(Long userId);

    @Transactional
    Optional<RefreshToken> findById(Long userId);
}
