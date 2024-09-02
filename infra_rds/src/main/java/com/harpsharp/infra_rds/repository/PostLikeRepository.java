package com.harpsharp.infra_rds.repository;

import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("SELECT p FROM PostLike p WHERE p.user.username = :username AND p.post.postId = :postId")
    Optional<PostLike> findByUsernameAndPostId(@Param("username") String username, @Param("postId") Long postId);
    List<PostLike> findByUser(User user);
    @Modifying
    @Query("UPDATE PostLike r SET r.user = null WHERE r.user = :user")
    void setLikesToNullByUser(User user);
}
