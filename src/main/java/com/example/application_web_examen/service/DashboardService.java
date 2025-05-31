package com.example.application_web_examen.service;

import com.example.application_web_examen.enums.ExamStatus;
import com.example.application_web_examen.enums.ResultStatus;
import com.example.application_web_examen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final ExamRepository examRepository;
    private final AssignedExamRepository assignedExamRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public DashboardService(StudentRepository studentRepository,
                            ProfessorRepository professorRepository,
                            ExamRepository examRepository,
                            AssignedExamRepository assignedExamRepository,
                            ResultRepository resultRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.examRepository = examRepository;
        this.assignedExamRepository = assignedExamRepository;
        this.resultRepository = resultRepository;
    }

    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Comptes utilisateurs
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalProfessors", professorRepository.count());

        // Examens
        stats.put("totalExams", examRepository.count());
        stats.put("activeExams", examRepository.findByIsActiveTrue().size());

        // Examens assignés
        stats.put("pendingExams", assignedExamRepository.findByStatus(ExamStatus.PENDING).size());
        stats.put("inProgressExams", assignedExamRepository.findByStatus(ExamStatus.IN_PROGRESS).size());
        stats.put("completedExams", assignedExamRepository.findByStatus(ExamStatus.COMPLETED).size());
        stats.put("expiredExams", assignedExamRepository.findByStatus(ExamStatus.EXPIRED).size());

        // Résultats
        stats.put("totalResults", resultRepository.count());
        stats.put("passedResults", resultRepository.findByStatus(ResultStatus.PASSED).size());
        stats.put("failedResults", resultRepository.findByStatus(ResultStatus.FAILED).size());

        return stats;
    }

    public Map<String, Object> getProfessorDashboardStats(Long professorId) {
        Map<String, Object> stats = new HashMap<>();

        // Examens du professeur
        int totalExams = examRepository.findByProfessorId(professorId).size();
        int activeExams = examRepository.findActivExamsByProfessor(professorId).size();

        stats.put("totalExams", totalExams);
        stats.put("activeExams", activeExams);
        stats.put("inactiveExams", totalExams - activeExams);

        // Examens assignés par ce professeur
        stats.put("totalAssignments", assignedExamRepository.findByProfessorId(professorId).size());
        stats.put("pendingAssignments", assignedExamRepository.findByProfessorId(professorId).stream()
                .mapToInt(ae -> ae.getStatus() == ExamStatus.PENDING ? 1 : 0).sum());
        stats.put("completedAssignments", assignedExamRepository.findByProfessorId(professorId).stream()
                .mapToInt(ae -> ae.getStatus() == ExamStatus.COMPLETED ? 1 : 0).sum());

        // Résultats des examens du professeur
        stats.put("totalResults", resultRepository.findByProfessorId(professorId).size());
        stats.put("passedResults", resultRepository.findByProfessorId(professorId).stream()
                .mapToInt(r -> r.getStatus() == ResultStatus.PASSED ? 1 : 0).sum());
        stats.put("failedResults", resultRepository.findByProfessorId(professorId).stream()
                .mapToInt(r -> r.getStatus() == ResultStatus.FAILED ? 1 : 0).sum());

        return stats;
    }

    public Map<String, Object> getStudentDashboardStats(Long studentId) {
        Map<String, Object> stats = new HashMap<>();

        // Examens assignés à l'étudiant
        int totalAssigned = assignedExamRepository.findByStudentId(studentId).size();
        int pending = (int) assignedExamRepository.findByStudentId(studentId).stream()
                .filter(ae -> ae.getStatus() == ExamStatus.PENDING).count();
        int completed = (int) assignedExamRepository.findByStudentId(studentId).stream()
                .filter(ae -> ae.getStatus() == ExamStatus.COMPLETED).count();
        int expired = (int) assignedExamRepository.findByStudentId(studentId).stream()
                .filter(ae -> ae.getStatus() == ExamStatus.EXPIRED).count();

        stats.put("totalAssignedExams", totalAssigned);
        stats.put("pendingExams", pending);
        stats.put("completedExams", completed);
        stats.put("expiredExams", expired);

        // Résultats de l'étudiant
        int totalResults = resultRepository.findByStudentId(studentId).size();
        int passed = (int) resultRepository.findByStudentId(studentId).stream()
                .filter(r -> r.getStatus() == ResultStatus.PASSED).count();
        int failed = (int) resultRepository.findByStudentId(studentId).stream()
                .filter(r -> r.getStatus() == ResultStatus.FAILED).count();

        stats.put("totalResults", totalResults);
        stats.put("passedExams", passed);
        stats.put("failedExams", failed);

        // Moyenne des scores
        double averageScore = resultRepository.findByStudentId(studentId).stream()
                .mapToDouble(r -> r.getScore() != null ? r.getScore() : 0.0)
                .average().orElse(0.0);

        stats.put("averageScore", Math.round(averageScore * 100.0) / 100.0);

        return stats;
    }

    public Map<String, Object> getRecentActivity(Long userId, String userType) {
        Map<String, Object> activity = new HashMap<>();
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);

        if ("PROFESSOR".equals(userType)) {
            // Examens récemment créés
            int recentExams = (int) examRepository.findByProfessorId(userId).stream()
                    .filter(e -> e.getCreatedAt().isAfter(lastWeek)).count();

            // Résultats récents
            int recentResults = (int) resultRepository.findByProfessorId(userId).stream()
                    .filter(r -> r.getDate().isAfter(lastWeek)).count();

            activity.put("recentExamsCreated", recentExams);
            activity.put("recentResults", recentResults);

        } else if ("STUDENT".equals(userType)) {
            // Examens récemment complétés
            int recentCompletions = (int) assignedExamRepository.findByStudentId(userId).stream()
                    .filter(ae -> ae.getStatus() == ExamStatus.COMPLETED)
                    .count();

            // Résultats récents
            int recentResults = (int) resultRepository.findByStudentId(userId).stream()
                    .filter(r -> r.getDate().isAfter(lastWeek)).count();

            activity.put("recentExamsCompleted", recentCompletions);
            activity.put("recentResults", recentResults);
        }

        return activity;
    }

    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();

        // Examens expirés qui n'ont pas été mis à jour
        int expiredNotUpdated = assignedExamRepository.findExpiredExams().size();

        // Emails non envoyés
        // Cette information devrait venir d'EmailService

        health.put("expiredExamsNotUpdated", expiredNotUpdated);
        health.put("status", expiredNotUpdated == 0 ? "HEALTHY" : "NEEDS_ATTENTION");

        return health;
    }
}