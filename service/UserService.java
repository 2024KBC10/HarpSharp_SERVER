package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.UserDTO;
import com.harpsharp.auth.entity.UserEntity;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(Long userId, UserDTO updatedDTO){
        String updatedUsername = updatedDTO.getUsername();
        String updatedPassword = updatedDTO.getPassword();
        String updatedEmail = updatedDTO.getEmail();

        if(userRepository.existsByUsername(updatedUsername)){
            throw UserAlreadyExistsException
                    .builder()
                    .code("USER_NAME_ALREADY_EXISTS")
                    .message("이미 존재하는 닉네임입니다.")
                    .build();
        }

        if(userRepository.existsByEmail(updatedEmail)){
            throw UserAlreadyExistsException
                    .builder()
                    .code("USER_EMAIL_ALREADY_EXISTS")
                    .message("이미 존재하는 이메일입니다.")
                    .build();
        }

        UserEntity existUser = userRepository
                .findById(userId)
                .orElseThrow(NullPointerException::new);

        UserEntity updatedUser = existUser.toBuilder()
                .username(updatedUsername)
                .password(passwordEncoder.encode(updatedPassword))
                .email(updatedEmail)
                .build();

        userRepository.save(updatedUser);
    }
}
