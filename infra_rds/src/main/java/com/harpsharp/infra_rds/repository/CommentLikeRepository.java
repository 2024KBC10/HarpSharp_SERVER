package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.CommentLike;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT p FROM CommentLike p WHERE p.user.username = :username AND p.comment.commentId = :commentId")
    @Lock(LockModeType.OPTIMISTIC)
    Optional<CommentLike> findByUsernameAndCommentId(@Param("username") String username, @Param("commentId") Long commentId);
    @Lock(LockModeType.OPTIMISTIC)
    List<CommentLike> findByUser(User user);
    @Modifying
    @Query("UPDATE PostLike r SET r.user = null WHERE r.user = :user")
    @Lock(LockModeType.OPTIMISTIC)
    void setLikesToNullByUser(User user);
}
