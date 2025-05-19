package com.nirob.todo.model.dto;

import com.nirob.todo.model.entity.Notify;
import com.nirob.todo.model.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Date is required")
    private LocalDate date;

    private LocalTime time;

    @NotBlank(message = "Priority is required")
    private Priority priority;

    @NotBlank(message = "Notify is required")
    private Notify notify;
}
