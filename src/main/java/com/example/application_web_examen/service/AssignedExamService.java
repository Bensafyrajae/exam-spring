package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.AssignedExamRequestDto;
import com.example.application_web_examen.dto.request.ExamAccessRequestDto;
import com.example.application_web_examen.dto.response.AssignedExamResponseDto;
import com.example.application_web_examen.enums.ExamStatus;
import com.example.application_web_examen.exception.ExamAccessException;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.AssignedExamMapper;
import com.example.application_web_examen.model.AssignedExam;
import com.example.application_web_examen.model.Exam;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.model.Student;
import com.example.application_web_examen.repository.AssignedExamRepository;
import com.example.application_web_examen.repository.ExamRepository;
import com.example.application_web_examen.repository.ProfessorRepository;
import com.example.application_web_examen.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignedExamService {

    private final AssignedExamRepository assignedExamRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AssignedExamMapper assignedExamMapper;
    private final EmailService emailService;

    @Autowired
    public AssignedExamService(AssignedExamRepository assignedExamRepository,
                               ExamRepository examRepository,
                               StudentRepository studentRepository,
                               ProfessorRepository professorRepository,
                               AssignedExamMapper assignedExamMapper,
                               EmailService emailService) {
        this.assignedExamRepository = assignedExamRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.assignedExamMapper = assignedExamMapper;
        this.emailService = emailService;
    }

    @Transactional
    public List<AssignedExamResponseDto> assignExamToStudents(AssignedExamRequestDto requestDto, Long professorId) {
        Exam exam = examRepository.findById(requestDto.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));

        // Verify professor owns the exam
        if (!exam.getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("You can only assign your own exams");
        }

        List<AssignedExam> assignedExams = new ArrayList<>();

        for (Long studentId : requestDto.getStudentIds()) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

            // Check if already assigned
            Optional<AssignedExam> existing = assignedExamRepository.findByStudentIdAndExamId(studentId, requestDto.getExamId());
            if (existing.isPresent()) {
                continue; // Skip if already assigned
            }

            AssignedExam assignedExam = new AssignedExam();
            assignedExam.setStudent(student);
            assignedExam.setExam(exam);
            assignedExam.setProfessor(professor);
            assignedExam.setFinalDate(requestDto.getFinalDate());
            assignedExam.setStatus(ExamStatus.PENDING);

            AssignedExam saved = assignedExamRepository.save(assignedExam);
            assignedExams.add(saved);

            // Send email notification
            try {
                emailService.sendExamAssignmentEmail(saved);
            } catch (Exception e) {
                // Log error but don't fail the assignment
                System.err.println("Failed to send email to " + student.getEmail() + ": " + e.getMessage());
            }
        }

        return assignedExams.stream()
                .map(assignedExamMapper::toAssignedExamResponseDto)
                .collect(Collectors.toList());
    }

    public AssignedExamResponseDto validateExamAccess(ExamAccessRequestDto accessRequest) {
        AssignedExam assignedExam = assignedExamRepository.findByUniqueLinkAndPasscode(
                        accessRequest.getUniqueLink(), accessRequest.getPasscode())
                .orElseThrow(() -> new ExamAccessException("Invalid access credentials"));

        if (assignedExam.isExpired()) {
            assignedExam.setStatus(ExamStatus.EXPIRED);
            assignedExamRepository.save(assignedExam);
            throw new ExamAccessException("Exam has expired");
        }

        if (!assignedExam.canTakeExam()) {
            throw new ExamAccessException("Exam cannot be taken at this time");
        }

        return assignedExamMapper.toAssignedExamResponseDto(assignedExam);
    }

    @Transactional
    public AssignedExamResponseDto startExam(Long assignedExamId) {
        AssignedExam assignedExam = assignedExamRepository.findById(assignedExamId)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned exam not found"));

        if (!assignedExam.canTakeExam()) {
            throw new ExamAccessException("Exam cannot be started");
        }

        assignedExam.setStatus(ExamStatus.IN_PROGRESS);
        assignedExam.setAttemptsMade(assignedExam.getAttemptsMade() + 1);

        AssignedExam updated = assignedExamRepository.save(assignedExam);
        return assignedExamMapper.toAssignedExamResponseDto(updated);
    }

    public List<AssignedExamResponseDto> getAssignedExamsByStudent(Long studentId) {
        List<AssignedExam> assignedExams = assignedExamRepository.findByStudentId(studentId);
        return assignedExams.stream()
                .map(assignedExamMapper::toAssignedExamResponseDto)
                .collect(Collectors.toList());
    }

    public List<AssignedExamResponseDto> getAssignedExamsByProfessor(Long professorId) {
        List<AssignedExam> assignedExams = assignedExamRepository.findByProfessorId(professorId);
        return assignedExams.stream()
                .map(assignedExamMapper::toAssignedExamResponseDto)
                .collect(Collectors.toList());
    }

    public List<AssignedExamResponseDto> getAssignedExamsByExam(Long examId) {
        List<AssignedExam> assignedExams = assignedExamRepository.findByExamId(examId);
        return assignedExams.stream()
                .map(assignedExamMapper::toAssignedExamResponseDto)
                .collect(Collectors.toList());
    }

    public AssignedExamResponseDto getAssignedExamById(Long id) {
        AssignedExam assignedExam = assignedExamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned exam not found"));
        return assignedExamMapper.toAssignedExamResponseDto(assignedExam);
    }

    @Transactional
    public void updateExpiredExams() {
        List<AssignedExam> expiredExams = assignedExamRepository.findExpiredExams();
        for (AssignedExam assignedExam : expiredExams) {
            assignedExam.setStatus(ExamStatus.EXPIRED);
        }
        if (!expiredExams.isEmpty()) {
            assignedExamRepository.saveAll(expiredExams);
        }
    }

    @Transactional
    public void cancelAssignment(Long assignedExamId, Long professorId) {
        AssignedExam assignedExam = assignedExamRepository.findById(assignedExamId)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned exam not found"));

        if (!assignedExam.getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("You can only cancel your own exam assignments");
        }

        if (assignedExam.getStatus() == ExamStatus.IN_PROGRESS || assignedExam.getStatus() == ExamStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel exam that is in progress or completed");
        }

        assignedExamRepository.delete(assignedExam);
    }

    public List<AssignedExamResponseDto> getPendingExams() {
        List<AssignedExam> pendingExams = assignedExamRepository.findByStatus(ExamStatus.PENDING);
        return pendingExams.stream()
                .map(assignedExamMapper::toAssignedExamResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendReminders() {
        List<AssignedExam> pendingExams = assignedExamRepository.findByStatus(ExamStatus.PENDING);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        for (AssignedExam assignedExam : pendingExams) {
            if (assignedExam.getFinalDate().isBefore(tomorrow) && assignedExam.getFinalDate().isAfter(LocalDateTime.now())) {
                try {
                    emailService.sendReminderEmail(assignedExam);
                } catch (Exception e) {
                    System.err.println("Failed to send reminder email: " + e.getMessage());
                }
            }
        }
    }
}