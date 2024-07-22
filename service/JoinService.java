package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.entity.UserEntity;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.auth.repository.RefreshRepository;
import com.harpsharp.auth.repository.UserRepository;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshRepository refreshRepository;

    public void registerUser(JoinDTO joinDTO, String role) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();
        //String profile_image = joinDTO.getProfile_image();

        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.builder()
                    .code("USER_ALREADY_EXISTS")
                    .message("User with email " + joinDTO.getEmail() + " already exists.")
                    .build();
        }

        if(userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.builder()
                    .code("USER_ALREADY_EXISTS")
                    .message("User with username " + joinDTO.getUsername() + " already exists.")
                    .build();
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .social_type("harp")
                .build();

        userRepository.save(user);
    }

}
