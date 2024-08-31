package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.board.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT p FROM CommentLike p WHERE p.user.username = :username AND p.comment.commentId = :commentId")
    Optional<CommentLike> findByUsernameAndPostId(@Param("username") String username, @Param("commentId") Long commentId);
}