package com.harpsharp.auth.service;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.dto.user.JoinDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.infra_rds.dto.user.RequestUserDTO;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.infra_rds.mapper.*;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;
    private final PostRepository postRepository;

    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final TodoCommentMapper todoCommentMapper;
    private final TodoPostMapper todoPostMapper;

    public Map<Long, ResponseUserDTO> registerUser(JoinDTO joinDTO, String role) {
        String username = joinDTO.username();
        String password = joinDTO.password();
        String email = joinDTO.email();

        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.builder()
                    .code("USER_ALREADY_EXISTS")
                    .message("User with email " + joinDTO.email() + " already exists.")
                    .build();
        }

        if(userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.builder()
                    .code("USER_ALREADY_EXISTS")
                    .message("User with username " + joinDTO.username() + " already exists.")
                    .build();
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .socialType("harp")
                .build();

        userRepository.save(user);
        return userMapper.toMap(user);
    }

    public void clear(){
        userRepository.deleteAll();
    }

    public void updateUser(Long userId, UpdateUserDTO updatedDTO){
        String updatedUsername = updatedDTO.updatedUsername();
        String updatedPassword = updatedDTO.updatedPassword();
        String updatedEmail = updatedDTO.updatedEmail();

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
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USER_ID"));

        if(!passwordEncoder.matches(updatedDTO.password(), existUser.getPassword())){
            throw new IllegalArgumentException("INVALID_PASSWORD");
        }

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

        if(updatedUser == null) throw new IllegalArgumentException("USER_NOT_FOUND");


        userRepository.save(existUser.updateUser(updatedUser));
    }

    public Map<Long, ResponseUserDTO> findById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USER_ID"));
        return userMapper.toMap(user);
    }

    public Map<Long, ResponseUserDTO> findByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USER_ID"));
        return userMapper.toMap(user);
    }

    public Map<Long, ResponseUserDTO> deleteById(Long userId, String accessToken){
        User deletedUser = userRepository
                .findById(userId)
                .orElseThrow(()->new IllegalArgumentException("INVALID_USER_ID"));

        userRepository.deleteById(userId);
        refreshTokenService.deleteByToken(accessToken);

        return userMapper.toMap(deletedUser);
    }

    public String findPasswordByUsername(String username){
        User existedUser = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));

        return existedUser.getPassword();
    }

    public Map<Long, ResponsePostDTO> findPostsByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));

        return postMapper.toMap(user.getPosts());
    }

    public Map<Long, ResponseCommentDTO> findCommentsByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));
        return commentMapper.toMap(user.getComments());
    }

    public Map<Long, ResponseTodoPostDTO> findTodoPostsByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));
        return todoPostMapper.toMap(user.getTodoPosts());
    }

    public Map<Long, ResponseTodoCommentDTO> findTodoCommentsByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));
        return todoCommentMapper.toMap(user.getTodoComments());
    }
}
