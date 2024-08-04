package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.dto.user.UserDTO;
import com.harpsharp.infra_rds.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public ResponseUserDTO convertUserToResponse(User user){
        List<ResponsePostDTO> postDTOs = postMapper.convertPostsToResponse(user.getPosts());
        List<ResponseCommentDTO> commentDTOs = commentMapper.convertCommentsToResponse(user.getComments());

        return new ResponseUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreated_at(),
                user.getUpdated_at(),
                user.getSocial_type(),
                user.getRole(),
                postDTOs,
                commentDTOs);
    }

    public UserDTO convertUserToDTO(User user){
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getSocial_type());
    }
}
