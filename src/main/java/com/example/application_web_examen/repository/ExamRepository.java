package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByProfessorId(Long professorId);
    List<Exam> findByIsActiveTrue();

    @Query("SELECT e FROM Exam e WHERE e.professor.id = :professorId AND e.isActive = true")
    List<Exam> findActivExamsByProfessor(Long professorId);
}