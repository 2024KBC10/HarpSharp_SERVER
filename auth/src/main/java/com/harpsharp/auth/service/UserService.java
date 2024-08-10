package com.harpsharp.auth.service;

import com.harpsharp.infra_rds.dto.user.JoinDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.infra_rds.dto.user.UserDTO;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.infra_rds.mapper.UserMapper;
import com.harpsharp.infra_rds.repository.PostRepository;
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
    private final UserMapper userMapper;
    private final PostRepository postRepository;

    public void registerUser(JoinDTO joinDTO, String role) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();

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

    public void updateUser(Long userId, UpdateUserDTO updatedDTO){
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

    public UserDTO findByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return userMapper.convertUserToDTO(user);
    }

    public void deleteById(Long userId, String accessToken){
        if(!userRepository.existsById(userId)){ throw new IllegalArgumentException("존재하지 않는 유저입니다."); }
        postRepository.deleteByUser(userRepository.findById(userId).orElseThrow(NullPointerException::new));
        userRepository.deleteById(userId);
        refreshTokenService.deleteByToken(accessToken);
    }

    public String findPasswordByUsername(String username){
        User existedUser = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));

        return existedUser.getPassword();
    }
}
