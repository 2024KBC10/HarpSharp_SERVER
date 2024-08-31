package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.comment.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.comment.RequestUpdateCommentDTO;
import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.mapper.CommentMapper;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;

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

    public Map<Long, ResponseCommentDTO> getCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));
        return commentMapper.toMap(comment);
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

        Comment comment = Comment
                .builder()
                .user(userRepository
                        .findByUsername(commentDTO.username())
                        .orElseThrow(()-> new IllegalArgumentException("USER_NOT_FOUND")))
                .content(commentDTO.content())
                .memoColor(commentDTO.memoColor())
                .pinColor(commentDTO.pinColor())
                .post(postRepository
                        .findById(commentDTO.postId())
                        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND")))
                .build();

        Comment savedComment = commentRepository.save(comment);
        Map<Long, ResponseCommentDTO> object = new HashMap<>();

        object.put(savedComment.getCommentId(),
                commentMapper.commentToResponseDTO(savedComment));
        return object;
    }

    public Map<Long, ResponseCommentDTO> updateComment(RequestUpdateCommentDTO commentDTO) {
        Comment existedCommnet = commentRepository
                .findById(commentDTO.commentId())
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        Comment updatedComment = existedCommnet
                .toBuilder()
                .content(commentDTO.content())
                .memoColor(commentDTO.memoColor())
                .pinColor(commentDTO.pinColor())
                .build();

        commentRepository.save(updatedComment);
        return commentMapper.toMap(updatedComment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        Post post = comment.getPost();

        if (post != null) {
            post.getComments().remove(comment);
            comment.clearPost();
        }

        commentRepository.delete(comment);
    }

    public Long likePost(RequestCommentLikeDTO requestCommentLikeDTO){
        Comment existedComment = commentRepository
                .findById(requestCommentLikeDTO.commentId())
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

        Comment updatedComment = existedComment
                .toBuilder()
                .likes(existedComment.getLikes()+1)
                .build();

        commentRepository.save(updatedComment);
        return updatedComment.getLikes();
    }

    public Long unlikePost(RequestCommentLikeDTO requestCommentLikeDTO){
        Comment existedComment = commentRepository
                .findById(requestCommentLikeDTO.commentId())
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));
        Long likeCount = max(0, existedComment.getLikes()-1);

        Comment updatedComment = existedComment
                .toBuilder()
                .likes(likeCount)
                .build();

        commentRepository.save(updatedComment);
        return updatedComment.getLikes();
    }

    public void clear(){
        commentRepository.deleteAll();
    }
}
