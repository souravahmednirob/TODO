package com.nirob.todo.repository;

import com.nirob.todo.model.entity.Priority;
import com.nirob.todo.model.entity.Status;
import com.nirob.todo.model.entity.TodoItem;
import com.nirob.todo.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByUser(User user);
    List<TodoItem> findByUserAndStatus(User user, Status status);
    List<TodoItem> findByUserAndPriority(User user, Priority priority);
    List<TodoItem> findByUserAndDate(User user, LocalDate date);
    Page<TodoItem> findByUser(User user, Pageable pageable);
}