package com.nirob.todo.controller;

import com.nirob.todo.model.dto.TodoItemRequestDto;
import com.nirob.todo.model.dto.TodoItemResponseDto;
import com.nirob.todo.model.entity.Priority;
import com.nirob.todo.model.entity.Status;
import com.nirob.todo.model.response.ApiResponse;
import com.nirob.todo.model.response.PagedResponse;
import com.nirob.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "API for todo operations")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "Create a new todo", description = "Create a new todo for the authenticated user")
    public ResponseEntity<ApiResponse<TodoItemResponseDto>> createTodo(
            @Valid @RequestBody TodoItemRequestDto todoDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TodoItemResponseDto createdTodo = todoService.createTodo(todoDto, userDetails.getUsername());
        ApiResponse<TodoItemResponseDto> response = ApiResponse.success("Todo created successfully", createdTodo);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a todo by ID", description = "Get a specific todo by its ID")
    public ResponseEntity<ApiResponse<TodoItemResponseDto>> getTodoById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TodoItemResponseDto todo = todoService.getTodoById(id, userDetails.getUsername());
        ApiResponse<TodoItemResponseDto> response = ApiResponse.success("Todo retrieved successfully", todo);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all todos", description = "Get all todos for the authenticated user")
    public ResponseEntity<ApiResponse<List<TodoItemResponseDto>>> getAllTodos(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<TodoItemResponseDto> todos = todoService.getAllTodosByUser(userDetails.getUsername());
        ApiResponse<List<TodoItemResponseDto>> response = ApiResponse.success("Todos retrieved successfully", todos);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated todos", description = "Get paginated todos for the authenticated user")
    public ResponseEntity<ApiResponse<PagedResponse<TodoItemResponseDto>>> getPaginatedTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        PagedResponse<TodoItemResponseDto> pagedTodos = todoService.getTodosByUserPaginated(
                userDetails.getUsername(), page, size);
        
        ApiResponse<PagedResponse<TodoItemResponseDto>> response = ApiResponse.success(
                "Paginated todos retrieved successfully", pagedTodos);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get todos by status", description = "Get todos filtered by status")
    public ResponseEntity<ApiResponse<List<TodoItemResponseDto>>> getTodosByStatus(
            @PathVariable Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<TodoItemResponseDto> todos = todoService.getTodosByStatus(status, userDetails.getUsername());
        ApiResponse<List<TodoItemResponseDto>> response = ApiResponse.success(
                "Todos by status retrieved successfully", todos);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get todos by priority", description = "Get todos filtered by priority")
    public ResponseEntity<ApiResponse<List<TodoItemResponseDto>>> getTodosByPriority(
            @PathVariable Priority priority,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<TodoItemResponseDto> todos = todoService.getTodosByPriority(priority, userDetails.getUsername());
        ApiResponse<List<TodoItemResponseDto>> response = ApiResponse.success(
                "Todos by priority retrieved successfully", todos);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get todos by date", description = "Get todos for a specific date")
    public ResponseEntity<ApiResponse<List<TodoItemResponseDto>>> getTodosByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<TodoItemResponseDto> todos = todoService.getTodosByDate(date, userDetails.getUsername());
        ApiResponse<List<TodoItemResponseDto>> response = ApiResponse.success(
                "Todos by date retrieved successfully", todos);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a todo", description = "Update a specific todo by its ID")
    public ResponseEntity<ApiResponse<TodoItemResponseDto>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoItemRequestDto todoDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TodoItemResponseDto updatedTodo = todoService.updateTodo(id, todoDto, userDetails.getUsername());
        ApiResponse<TodoItemResponseDto> response = ApiResponse.success("Todo updated successfully", updatedTodo);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update todo status", description = "Update the status of a specific todo")
    public ResponseEntity<ApiResponse<TodoItemResponseDto>> updateTodoStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        TodoItemResponseDto updatedTodo = todoService.updateTodoStatus(id, status, userDetails.getUsername());
        ApiResponse<TodoItemResponseDto> response = ApiResponse.success("Todo status updated successfully", updatedTodo);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a todo", description = "Delete a specific todo by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        todoService.deleteTodo(id, userDetails.getUsername());
        ApiResponse<Void> response = ApiResponse.success("Todo deleted successfully", null);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}