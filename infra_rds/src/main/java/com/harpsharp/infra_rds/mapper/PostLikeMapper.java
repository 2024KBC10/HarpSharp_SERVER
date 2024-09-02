package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.board.like.ResponsePostLikeDTO;
import com.harpsharp.infra_rds.entity.board.Post;
import com.harpsharp.infra_rds.entity.board.PostLike;
import com.harpsharp.infra_rds.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostLikeMapper {
    public ResponsePostLikeDTO postLikeToResponseDTO(PostLike postLike){
        User user = postLike.getUser();
        Post post = postLike.getPost();

        return new ResponsePostLikeDTO(
                user.getUsername(),
                post.getPostId(),
                post.getLikes()
        );
    }

    public List<ResponsePostLikeDTO> postLikeToList(List<PostLike> postLikes) {
        if(postLikes == null) return new ArrayList<>();

        return postLikes.stream()
                .map(this::postLikeToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponsePostLikeDTO> profilesToMap(List<PostLike> postLikes) {
        if(postLikes == null) return new HashMap<>();
        return postLikes.stream()
                .collect(Collectors.toMap(PostLike::getId, this::postLikeToResponseDTO));
    }
}
