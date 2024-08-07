package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.entity.Comment;
import com.harpsharp.infra_rds.entity.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    public ResponseCommentDTO commentToResponseDTO(Comment comment){
        return new ResponseCommentDTO(
                comment.getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }

    public List<ResponseCommentDTO> convertCommentsToResponse(List<Comment> comments) {
        return comments.stream() // 스트림 시작
                .map(this::commentToResponseDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList()); // 결과를 List로 수집
    }

    public Map<Long, ResponseCommentDTO> toMap(List<Comment> comments) {
        return comments.stream().collect(
                Collectors.toMap(Comment::getCommentId, this::commentToResponseDTO));
    }
}
