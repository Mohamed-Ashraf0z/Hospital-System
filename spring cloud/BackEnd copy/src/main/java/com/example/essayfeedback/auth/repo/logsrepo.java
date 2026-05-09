package com.example.essayfeedback.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.essayfeedback.auth.entity.Logs;
import java.util.Optional;

@Repository
public interface logsrepo extends JpaRepository<Logs, Long> {
    // Optional<Student> findByUser(User user);
}