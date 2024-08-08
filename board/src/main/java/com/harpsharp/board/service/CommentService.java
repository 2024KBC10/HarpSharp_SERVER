package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.RequestUpdateCommnetDTO;
import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.entity.Comment;
import com.harpsharp.infra_rds.entity.Post;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.mapper.CommentMapper;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public Map<Long, ResponseCommentDTO> getCommentsByPostId(Long postId) {
        Post rootPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        return commentMapper.toMap(rootPost.getComments());
    }

    public Optional<Comment> getCommentById(Long id) {

        return commentRepository.findById(id);
    }

    public Map<Long, ResponseCommentDTO> findCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new IllegalArgumentException("POST_NOT_FOUND"));
        return commentMapper.toMap(post.getComments());
    }

    public Map<Long, ResponseCommentDTO> findCommentByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("COMMENT_NOT_FOUND"));
        Map<Long, ResponseCommentDTO> object = new HashMap<>();
        object.put(commentId, commentMapper.commentToResponseDTO(comment));
        return object;
    }

    public Map<Long, ResponseCommentDTO> save(RequestCommentDTO commentDTO) {
        Post rootPost = postRepository
                .findById(commentDTO.postId())
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        User author = userRepository
                .findByUsername(commentDTO.username())
                .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND"));

        Comment comment = Comment
                .builder()
                .user(author)
                .content(commentDTO.content())
                .post(rootPost)
                .build();

        Comment savedComment = commentRepository.save(comment);
        Map<Long, ResponseCommentDTO> object = new HashMap<>();

        object.put(savedComment.getCommentId(),
                commentMapper.commentToResponseDTO(savedComment));
        return object;
    }

    public Map<Long, ResponseCommentDTO> updateComment(RequestUpdateCommnetDTO commentDTO) {
        Comment existedCommnet = commentRepository
                .findById(commentDTO.commentId())
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        existedCommnet = existedCommnet
                .toBuilder()
                .content(commentDTO.content())
                .build();

        commentRepository.save(existedCommnet);
        Map<Long, ResponseCommentDTO> object = new HashMap<>();
        object.put(existedCommnet.getCommentId(),
                commentMapper.commentToResponseDTO(existedCommnet));

        return object;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
