package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("SELECT p FROM PostLike p WHERE p.user.username = :username AND p.post.postId = :postId")
    @Lock(LockModeType.OPTIMISTIC)
    Optional<PostLike> findByUsernameAndPostId(@Param("username") String username, @Param("postId") Long postId);

    @Lock(LockModeType.OPTIMISTIC)
    List<PostLike> findByUser(User user);
}
