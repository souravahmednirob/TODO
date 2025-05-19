package com.nirob.todo.service;

import com.nirob.todo.model.dto.TodoItemRequestDto;
import com.nirob.todo.model.dto.TodoItemResponseDto;
import com.nirob.todo.model.entity.Priority;
import com.nirob.todo.model.entity.Status;
import com.nirob.todo.model.response.PagedResponse;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
    TodoItemResponseDto createTodo(TodoItemRequestDto todoDto, String userEmail);
    TodoItemResponseDto getTodoById(Long id, String userEmail);
    List<TodoItemResponseDto> getAllTodosByUser(String userEmail);
    PagedResponse<TodoItemResponseDto> getTodosByUserPaginated(String userEmail, int page, int size);
    List<TodoItemResponseDto> getTodosByStatus(Status status, String userEmail);
    List<TodoItemResponseDto> getTodosByPriority(Priority priority, String userEmail);
    List<TodoItemResponseDto> getTodosByDate(LocalDate date, String userEmail);
    TodoItemResponseDto updateTodo(Long id, TodoItemRequestDto todoDto, String userEmail);
    TodoItemResponseDto updateTodoStatus(Long id, Status status, String userEmail);
    void deleteTodo(Long id, String userEmail);
}