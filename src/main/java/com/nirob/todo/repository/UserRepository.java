package com.nirob.todo.repository;

import com.nirob.todo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);
    boolean existsByMail(String mail);
}
