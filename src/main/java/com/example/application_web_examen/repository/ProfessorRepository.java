package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsername(String username);
    Optional<Professor> findByEmail(String email);
}