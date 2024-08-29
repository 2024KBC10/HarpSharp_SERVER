package com.harpsharp.auth.service;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.dto.user.JoinDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.infra_rds.dto.user.Position;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.mapper.*;
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

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final TodoCommentMapper todoCommentMapper;
    private final TodoPostMapper todoPostMapper;

    public Map<Long, ResponseUserDTO> registerUser(JoinDTO joinDTO, String role) {
        String username = joinDTO.username();
        String password = joinDTO.password();
        String email = joinDTO.email();
        Position position = joinDTO.position();

        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.builder()
                    .code(409)
                    .message("USER_ALREADY_EXISTS")
                    .details("이미 존재하는 이메일입니다.")
                    .build();
        }

        if(userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.builder()
                    .code(409)
                    .message("USER_ALREADY_EXISTS")
                    .message("이미 존재하는 이메일입니다.")
                    .build();
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .position(position)
                .role(role)
                .socialType("harp")
                .build();

        userRepository.save(user);
        System.out.println("userMapper.toMap(user) = " + userMapper.toMap(user));
        
        return userMapper.toMap(user);
    }

    public void clear(){
        userRepository.deleteAll();
    }

    public Map<Long, ResponseUserDTO> updateUser(String username, UpdateUserDTO updatedDTO){
        String updatedUsername = updatedDTO.updatedUsername();
        String updatedPassword = updatedDTO.updatedPassword();
        String updatedEmail = updatedDTO.updatedEmail();

        User existUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USERNAME"));

        if(!passwordEncoder.matches(updatedDTO.password(), existUser.getPassword())){
            throw new IllegalArgumentException("INVALID_PASSWORD");
        }

        if(userRepository.existsByUsername(updatedUsername)){
            throw UserAlreadyExistsException
                    .builder()
                    .code(409)
                    .message("USER_ALREADY_EXISTS")
                    .message("이미 존재하는 닉네임입니다.")
                    .build();
        }

        if(userRepository.existsByEmail(updatedEmail)){
            throw UserAlreadyExistsException
                    .builder()
                    .code(409)
                    .message("USER_ALREADY_EXISTS")
                    .details("이미 존재하는 이메일입니다.")
                    .build();
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

        existUser.setUsername(updatedUsername);
        existUser.setPassword(passwordEncoder.encode(updatedPassword));
        existUser.setEmail(updatedEmail);

        userRepository.save(existUser);

        return userMapper.toMap(existUser);
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

    public Map<Long, ResponseUserDTO> deleteByUsername(String username, String accessToken){
        User deletedUser = userRepository
                .findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("INVALID_USER_ID"));

        userRepository.deleteById(deletedUser.getUserId());

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
