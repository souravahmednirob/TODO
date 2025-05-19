package com.nirob.todo.model.dto;

import com.nirob.todo.model.entity.Notify;
import com.nirob.todo.model.entity.Priority;
import com.nirob.todo.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemResponseDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private Priority priority;
    private Status status;
    private Notify notify;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}