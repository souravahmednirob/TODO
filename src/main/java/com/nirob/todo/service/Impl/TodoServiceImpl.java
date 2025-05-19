package com.nirob.todo.service.Impl;

import com.nirob.todo.exception.ResourceNotFoundException;
import com.nirob.todo.exception.UnauthorizedException;
import com.nirob.todo.model.dto.TodoItemRequestDto;
import com.nirob.todo.model.dto.TodoItemResponseDto;
import com.nirob.todo.model.entity.Priority;
import com.nirob.todo.model.entity.Status;
import com.nirob.todo.model.entity.TodoItem;
import com.nirob.todo.model.entity.User;
import com.nirob.todo.model.response.PagedResponse;
import com.nirob.todo.repository.TodoItemRepository;
import com.nirob.todo.service.TodoService;
import com.nirob.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoItemRepository todoItemRepository;
    private final UserService userService;

    @Override
    public TodoItemResponseDto createTodo(TodoItemRequestDto todoDto, String userEmail) {
        User user = userService.findByMail(userEmail);
        
        TodoItem todoItem = new TodoItem();
        todoItem.setUser(user);
        todoItem.setTitle(todoDto.getTitle());
        todoItem.setDescription(todoDto.getDescription());
        todoItem.setDate(todoDto.getDate());
        todoItem.setTime(todoDto.getTime());
        todoItem.setPriority(todoDto.getPriority());
        todoItem.setNotify(todoDto.getNotify());
        todoItem.setStatus(Status.PENDING);
        
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        
        return mapToResponseDto(savedTodoItem);
    }

    @Override
    public TodoItemResponseDto getTodoById(Long id, String userEmail) {
        User user = userService.findByMail(userEmail);
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        // Check if the todo belongs to the user
        if (!todoItem.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to access this todo");
        }

        return mapToResponseDto(todoItem);
    }


    @Override
    public List<TodoItemResponseDto> getAllTodosByUser(String userEmail) {
        User user = userService.findByMail(userEmail);
        List<TodoItem> todoItems = todoItemRepository.findByUser(user);
        
        return todoItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<TodoItemResponseDto> getTodosByUserPaginated(String userEmail, int page, int size) {
        User user = userService.findByMail(userEmail);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").ascending());
        Page<TodoItem> todoItems = todoItemRepository.findByUser(user, pageable);
        
        List<TodoItemResponseDto> content = todoItems.getContent().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        
        return PagedResponse.<TodoItemResponseDto>builder()
                .content(content)
                .page(todoItems.getNumber())
                .size(todoItems.getSize())
                .totalElements(todoItems.getTotalElements())
                .totalPages(todoItems.getTotalPages())
                .last(todoItems.isLast())
                .build();
    }

    @Override
    public List<TodoItemResponseDto> getTodosByStatus(Status status, String userEmail) {
        User user = userService.findByMail(userEmail);
        List<TodoItem> todoItems = todoItemRepository.findByUserAndStatus(user, status);
        
        return todoItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoItemResponseDto> getTodosByPriority(Priority priority, String userEmail) {
        User user = userService.findByMail(userEmail);
        List<TodoItem> todoItems = todoItemRepository.findByUserAndPriority(user, priority);
        
        return todoItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoItemResponseDto> getTodosByDate(LocalDate date, String userEmail) {
        User user = userService.findByMail(userEmail);
        List<TodoItem> todoItems = todoItemRepository.findByUserAndDate(user, date);
        
        return todoItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoItemResponseDto updateTodo(Long id, TodoItemRequestDto todoDto, String userEmail) {
        User user = userService.findByMail(userEmail);
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        // Check if the todo belongs to the user
        if (!todoItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this todo");
        }
        
        todoItem.setTitle(todoDto.getTitle());
        todoItem.setDescription(todoDto.getDescription());
        todoItem.setDate(todoDto.getDate());
        todoItem.setTime(todoDto.getTime());
        todoItem.setPriority(todoDto.getPriority());
        todoItem.setNotify(todoDto.getNotify());
        
        TodoItem updatedTodoItem = todoItemRepository.save(todoItem);
        
        return mapToResponseDto(updatedTodoItem);
    }

    @Override
    public TodoItemResponseDto updateTodoStatus(Long id, Status status, String userEmail) {
        User user = userService.findByMail(userEmail);
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        // Check if the todo belongs to the user
        if (!todoItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this todo");
        }
        
        todoItem.setStatus(status);
        
        // If status is COMPLETED, set completedAt
        if (status == Status.COMPLETED) {
            todoItem.setCompletedAt(LocalDateTime.now());
        } else {
            todoItem.setCompletedAt(null);
        }
        
        TodoItem updatedTodoItem = todoItemRepository.save(todoItem);
        
        return mapToResponseDto(updatedTodoItem);
    }

    @Override
    public void deleteTodo(Long id, String userEmail) {
        User user = userService.findByMail(userEmail);
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        // Check if the todo belongs to the user
        if (!todoItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this todo");
        }
        
        todoItemRepository.delete(todoItem);
    }
    
    // Helper method to map TodoItem entity to TodoItemResponseDto
    private TodoItemResponseDto mapToResponseDto(TodoItem todoItem) {
        return TodoItemResponseDto.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .description(todoItem.getDescription())
                .date(todoItem.getDate())
                .time(todoItem.getTime())
                .priority(todoItem.getPriority())
                .status(todoItem.getStatus())
                .notify(todoItem.getNotify())
                .completedAt(todoItem.getCompletedAt())
                .createdAt(todoItem.getCreatedAt())
                .updatedAt(todoItem.getUpdatedAt())
                .build();
    }
}