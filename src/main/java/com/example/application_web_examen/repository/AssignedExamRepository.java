package com.example.application_web_examen.repository;

import com.example.application_web_examen.enums.ExamStatus;
import com.example.application_web_examen.model.AssignedExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedExamRepository extends JpaRepository<AssignedExam, Long> {
    List<AssignedExam> findByStudentId(Long studentId);
    List<AssignedExam> findByExamId(Long examId);
    List<AssignedExam> findByProfessorId(Long professorId);
    List<AssignedExam> findByStatus(ExamStatus status);

    Optional<AssignedExam> findByUniqueLink(String uniqueLink);
    Optional<AssignedExam> findByUniqueLinkAndPasscode(String uniqueLink, String passcode);

    @Query("SELECT ae FROM AssignedExam ae WHERE ae.student.id = :studentId AND ae.exam.id = :examId")
    Optional<AssignedExam> findByStudentIdAndExamId(Long studentId, Long examId);

    @Query("SELECT ae FROM AssignedExam ae WHERE ae.finalDate < CURRENT_TIMESTAMP AND ae.status != 'COMPLETED' AND ae.status != 'EXPIRED'")
    List<AssignedExam> findExpiredExams();
}