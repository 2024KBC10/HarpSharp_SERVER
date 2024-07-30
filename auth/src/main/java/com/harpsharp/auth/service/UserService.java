package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.dto.UserDTO;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;


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

    public void clear(){
        userRepository.deleteAll();
    }

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

        User existUser = userRepository
                .findById(userId)
                .orElseThrow(NullPointerException::new);

        if(updatedUsername == null){
            updatedUsername = existUser.getUsername();
        }
        if(updatedPassword == null){
            updatedPassword = existUser.getPassword();
        }
        if(updatedEmail == null){
            updatedEmail = existUser.getEmail();
        }

        User updatedUser = existUser.toBuilder()
                .username(updatedUsername)
                .password(passwordEncoder.encode(updatedPassword))
                .email(updatedEmail)
                .build();

        if(updatedUser == null) throw new IllegalArgumentException();


        userRepository.save(existUser.updateUser(updatedUser));
    }

    public Optional<User> findById(Long userId){ return userRepository.findById(userId); }

    public void deleteById(Long userId, String accessToken){
        if(!userRepository.existsById(userId)){ throw new IllegalArgumentException("존재하지 않는 유저입니다."); }

        userRepository.deleteById(userId);
        refreshTokenService.deleteByToken(accessToken);

    }
}
