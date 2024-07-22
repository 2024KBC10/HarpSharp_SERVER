package com.harpsharp.auth.repository;

import com.harpsharp.auth.entity.UserEntity;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findByEmailAndPassword(String email, String password);
    boolean existsByUsername(String username);
}
