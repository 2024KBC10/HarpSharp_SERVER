package com.harpsharp.todo.service;

import com.harpsharp.infra_rds.dto.todo.post.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.post.RequestTodoPostUpdateStatusDTO;
import com.harpsharp.infra_rds.dto.todo.comment.RequestUpdateTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.post.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.entity.todo.TodoPost;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.mapper.TodoPostMapper;
import com.harpsharp.infra_rds.repository.TodoCommentRepository;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoPostService {
    private final UserRepository userRepository;
    private final TodoPostRepository todoPostRepository;
    private final TodoPostMapper todoPostMapper;
    private final TodoCommentRepository todoCommentRepository;

    public Map<Long, ResponseTodoPostDTO> getAllTodoPosts() {
        List<TodoPost> posts = todoPostRepository.findAll();
        return todoPostMapper.toMap(posts);
    }

    public Map<Long,ResponseTodoPostDTO> getTodoPostById(Long id) {
        TodoPost post = todoPostRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoPost_NOT_FOUND"));

        return todoPostMapper.toMap(post);
    }

    public Map<Long, ResponseTodoPostDTO> getTodoPostByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("USER_NOT_FOUND"));
        List<TodoPost> postByUsername = todoPostRepository.findByUser(user);

        return todoPostMapper.toMap(postByUsername);
    }

    public Map<Long,ResponseTodoPostDTO> saveTodoPost(RequestTodoPostDTO requestTodoPostDTO) {
        TodoPost todoPost = todoPostRepository.save(todoPostMapper.requestToEntity(requestTodoPostDTO));
        return todoPostMapper.toMap(todoPost);
    }

    public Map<Long,ResponseTodoPostDTO> updateTodoPost(RequestUpdateTodoPostDTO updatedPostDTO) {
        Long postId = updatedPostDTO.postId();

        TodoPost existedPost = todoPostRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        TodoPost updatedPost = existedPost
                .toBuilder()
                .title(updatedPostDTO.title())
                .content(updatedPostDTO.content())
                .status(updatedPostDTO.status())
                .startAt(updatedPostDTO.startAt())
                .endAt(updatedPostDTO.endAt())
                .build();

        return todoPostMapper.toMap(todoPostRepository.save(updatedPost));
    }

    public Map<Long, ResponseTodoPostDTO> updateTodoStatus(RequestTodoPostUpdateStatusDTO updatedStatusDTO){
        Long postId = updatedStatusDTO.postId();
        TodoPost existedPost = todoPostRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

        TodoPost updatedPost = existedPost
                .toBuilder()
                .status(updatedStatusDTO.status())
                .build();

        return todoPostMapper.toMap(todoPostRepository.save(updatedPost));
    }

    public void deleteTodoPost(Long id) {
        TodoPost post = todoPostRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));
        todoCommentRepository.deleteByTodoPost(post);
        todoPostRepository.deleteById(id);
    }

    public void clear(){
        todoPostRepository.deleteAll();
    }

}
