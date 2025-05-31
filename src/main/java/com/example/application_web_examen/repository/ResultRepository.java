package com.example.application_web_examen.repository;

import com.example.application_web_examen.enums.ResultStatus;
import com.example.application_web_examen.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(Long studentId);
    List<Result> findByExamId(Long examId);
    List<Result> findByStatus(ResultStatus status);

    Optional<Result> findByStudentIdAndExamId(Long studentId, Long examId);

    @Query("SELECT r FROM Result r WHERE r.exam.professor.id = :professorId")
    List<Result> findByProfessorId(Long professorId);

    @Query("SELECT AVG(r.score) FROM Result r WHERE r.exam.id = :examId")
    Double findAverageScoreByExamId(Long examId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.exam.id = :examId AND r.status = 'PASSED'")
    Long countPassedByExamId(Long examId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.exam.id = :examId AND r.status = 'FAILED'")
    Long countFailedByExamId(Long examId);
}