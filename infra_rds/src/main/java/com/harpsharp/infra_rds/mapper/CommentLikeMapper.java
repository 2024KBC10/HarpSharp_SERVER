package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.like.ResponseCommentLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommentLikeMapper {
    public ResponseCommentLikeDTO commentLikeToResponseDTO(CommentLike commentLike){
        User user = commentLike.getUser();
        Comment comment = commentLike.getComment();

        return new ResponseCommentLikeDTO(
                user.getUsername(),
                comment.getCommentId(),
                comment.getLikes()
        );
    }

    public List<Long> commentLikesToListOfId(List<CommentLike> commentLikes){
        if(commentLikes == null || commentLikes.isEmpty()){ return new ArrayList<>(); }
        return commentLikes
                .stream()
                .map(CommentLike::getCommentId)
                .collect(Collectors.toList());
    }

    public List<ResponseCommentLikeDTO> commentLikeToList(List<CommentLike> commentLikes) {
        if(commentLikes == null) return new ArrayList<>();

        return commentLikes.stream()
                .map(this::commentLikeToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseCommentLikeDTO> commentLikeToMap(List<CommentLike> commentLikes) {
        if(commentLikes == null || commentLikes.isEmpty()) return new HashMap<>();
        return commentLikes.stream()
                .collect(Collectors.toMap(CommentLike::getId, this::commentLikeToResponseDTO));
    }
}
