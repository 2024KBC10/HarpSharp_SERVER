package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.comment.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment requestToComment(RequestCommentDTO commentDTO){
        User user =  userRepository
                .findByUsername(commentDTO.username())
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));

        Post post = postRepository
                .findById(commentDTO.postId())
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        return Comment
                .builder()
                .user(user)
                .content(commentDTO.content())
                .memoColor(commentDTO.memoColor())
                .pinColor(commentDTO.pinColor())
                .post(post)
                .build();

    }

    public ResponseCommentDTO commentToResponseDTO(Comment comment){
        return new ResponseCommentDTO(
                comment.getUsername(),
                comment.getContent(),
                comment.getMemoColor(),
                comment.getPinColor(),
                comment.getLikes(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }

    public List<ResponseCommentDTO> convertCommentsToResponse(List<Comment> comments) {
        if(comments == null) return new ArrayList<>();
        return comments.stream() // 스트림 시작
                .map(this::commentToResponseDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList()); // 결과를 List로 수집
    }

    public Map<Long, ResponseCommentDTO> toMap(List<Comment> comments) {
        if(comments == null) return new HashMap<>();

        return comments.stream().collect(
                Collectors.toMap(Comment::getCommentId, this::commentToResponseDTO));
    }

    public Map<Long, ResponseCommentDTO> toMap(Comment comments) {
        Map<Long, ResponseCommentDTO> object = new HashMap<>();
        object.put(comments.getCommentId(), commentToResponseDTO(comments));
        return object;
    }
}
