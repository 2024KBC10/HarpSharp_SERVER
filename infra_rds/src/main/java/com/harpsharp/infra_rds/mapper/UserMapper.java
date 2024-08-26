package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoCommentDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.RequestUserDTO;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
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
        String url = null;
        ProfileImage profileImage = user.getProfileImage();

        if(profileImage != null) url = profileImage.getUrl();

        return new ResponseUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getSocialType(),
                user.getRole(),
                url,
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
        String url = null;
        ProfileImage profileImage = user.getProfileImage();

        if(profileImage != null) url = profileImage.getUrl();

        return new ResponseUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getSocialType(),
                user.getRole(),
                url,
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
        return users.stream().collect(
                Collectors.toMap(User::getUserId, this::userToResponseDTO));
    }

    public List<ResponseUserDTO> mapToList(Map<Long, ResponseUserDTO> object){
        return new ArrayList<>(object.values());
    }

}
