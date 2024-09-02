package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.post.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.comment.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.post.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.RequestUserDTO;
import com.harpsharp.infra_rds.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final TodoPostMapper todoPostMapper;
    private final TodoCommentMapper todoCommentMapper;

    public ResponseUserDTO convertUserToResponse(User user){
        Map<Long, ResponsePostDTO> postDTOs = postMapper.toMap(user.getPosts());
        Map<Long, ResponseCommentDTO> commentDTOs = commentMapper.toMap(user.getComments());
        Map<Long, ResponseTodoPostDTO> todoPostDTOs = todoPostMapper.toMap(user.getTodoPosts());
        Map<Long, ResponseTodoCommentDTO> todoCommentDTOs = todoCommentMapper.toMap(user.getTodoComments());

        return new ResponseUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getPosition(),
                user.getSocialType(),
                user.getRole(),
                postDTOs,
                commentDTOs,
                todoPostDTOs,
                todoCommentDTOs);
    }

    public RequestUserDTO userToRequestDTO(User user){
        return new RequestUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRole());
    }

    public ResponseUserDTO userToResponseDTO(User user){
        return new ResponseUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getPosition(),
                user.getSocialType(),
                user.getRole(),
                postMapper.toMap(user.getPosts()),
                commentMapper.toMap(user.getComments()),
                todoPostMapper.toMap(user.getTodoPosts()),
                todoCommentMapper.toMap(user.getTodoComments())
        );
    }

    public Map<Long, ResponseUserDTO> toMap(User user){
        Long userId = user.getUserId();
        Map<Long, ResponseUserDTO> object = new HashMap<>();
        object.put(userId, convertUserToResponse(user));

        return object;
    }

    public Map<Long, ResponseUserDTO> toMap(List<User> users){
        if(users == null) return new HashMap<>();

        return users.stream().collect(
                Collectors.toMap(User::getUserId, this::userToResponseDTO));
    }

    public List<ResponseUserDTO> mapToList(Map<Long, ResponseUserDTO> object){
        return new ArrayList<>(object.values());
    }

}
