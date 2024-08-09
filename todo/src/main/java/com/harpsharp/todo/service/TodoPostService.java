package com.harpsharp.todo.service;

import com.harpsharp.infra_rds.dto.todo.RequestTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.RequestUpdateTodoPostDTO;
import com.harpsharp.infra_rds.dto.todo.ResponseTodoPostDTO;
import com.harpsharp.infra_rds.entity.TodoPost;
import com.harpsharp.infra_rds.mapper.TodoPostMapper;
import com.harpsharp.infra_rds.repository.TodoPostRepository;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoPostService {
    private final UserRepository userRepository;
    private final TodoPostRepository todoPostRepository;
    private final TodoPostMapper todoPostMapper;

    public List<ResponseTodoPostDTO> getAllTodoPosts() {
        return todoPostRepository.findAll().stream()
                .map(todoPostMapper::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResponseTodoPostDTO> getTodoPostsByUsername(String username) {
        return todoPostRepository.findByUser(
                        userRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                ).stream().map(todoPostMapper::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResponseTodoPostDTO> getTodoPostById(Long id) {
        return todoPostRepository.findById(id)
                .map(todoPostMapper::entityToResponseDTO);
    }

    public ResponseTodoPostDTO createTodoPost(RequestTodoPostDTO requestTodoPostDTO) {
        TodoPost todoPost = todoPostMapper.requestToEntity(requestTodoPostDTO);
        return todoPostMapper.entityToResponseDTO(todoPostRepository.save(todoPost));
    }

    public ResponseTodoPostDTO updateTodoPost(Long id, RequestUpdateTodoPostDTO requestUpdateTodoPostDTO) {
        TodoPost todoPost = todoPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TodoPost Id:" + id));
        todoPost = todoPostMapper.updateRequestToEntity(todoPost, requestUpdateTodoPostDTO);
        return todoPostMapper.entityToResponseDTO(todoPostRepository.save(todoPost));
    }

    public void deletePost(Long id) {
        todoPostRepository.deleteById(id);
    }
}
