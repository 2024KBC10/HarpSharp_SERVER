package com.harpsharp.board.service;

import com.harpsharp.infra_rds.dto.board.comment.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.comment.RequestUpdateCommentDTO;
import com.harpsharp.infra_rds.dto.board.comment.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestCommentLikeDTO;
import com.harpsharp.infra_rds.dto.board.like.RequestPostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Comment;
import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.mapper.CommentMapper;
import com.harpsharp.infra_rds.repository.CommentLikeRepository;
import com.harpsharp.infra_rds.repository.CommentRepository;
import com.harpsharp.infra_rds.repository.PostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    private final CommentLikeRepository commentLikeRepository;

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

    public Map<Long, ResponseCommentDTO> addComment(RequestCommentDTO commentDTO) {

        Comment comment = commentMapper.requestToComment(commentDTO);

        Post post = postRepository
                .findById(commentDTO.postId())
                .orElseThrow(()->new IllegalArgumentException("POST_NOT_FOUND"));

        post.addComment(comment);
        postRepository.save(post);

        List<Comment> comments = post.getComments();

        return commentMapper.toMap(comments.get(comments.size()-1));
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

        post.removeComment(comment);
        postRepository.save(post);
    }

    public void clear(){
        commentRepository.deleteAll();
    }
}
