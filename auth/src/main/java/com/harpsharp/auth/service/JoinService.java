package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JoinService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JoinService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .social_type("harp")
                .build();

        userRepository.save(user);
    }
}
