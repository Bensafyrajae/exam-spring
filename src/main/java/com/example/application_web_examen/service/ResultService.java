package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.ExamSubmissionRequestDto;
import com.example.application_web_examen.dto.response.ResultResponseDto;
import com.example.application_web_examen.enums.ExamStatus;
import com.example.application_web_examen.enums.ResultStatus;
import com.example.application_web_examen.exception.ExamSubmissionException;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.ResultMapper;
import com.example.application_web_examen.model.*;
import com.example.application_web_examen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final AssignedExamRepository assignedExamRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ResultMapper resultMapper;
    private final EmailService emailService;

    @Autowired
    public ResultService(ResultRepository resultRepository,
                         AssignedExamRepository assignedExamRepository,
                         QuestionRepository questionRepository,
                         AnswerRepository answerRepository,
                         ResultMapper resultMapper,
                         EmailService emailService) {
        this.resultRepository = resultRepository;
        this.assignedExamRepository = assignedExamRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.resultMapper = resultMapper;
        this.emailService = emailService;
    }

    @Transactional
    public ResultResponseDto submitExam(ExamSubmissionRequestDto submissionDto) {
        AssignedExam assignedExam = assignedExamRepository.findById(submissionDto.getAssignedExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned exam not found"));

        if (assignedExam.getStatus() != ExamStatus.IN_PROGRESS) {
            throw new ExamSubmissionException("Exam is not in progress");
        }

        // Check if result already exists
        if (resultRepository.findByStudentIdAndExamId(assignedExam.getStudent().getId(), assignedExam.getExam().getId()).isPresent()) {
            throw new ExamSubmissionException("Exam already submitted");
        }

        // Create result
        Result result = new Result();
        result.setExam(assignedExam.getExam());
        result.setStudent(assignedExam.getStudent());
        result.setStudentAnswers(submissionDto.getAnswers());
        result.setDate(LocalDateTime.now());

        // Calculate score
        List<Question> examQuestions = questionRepository.findByExamId(assignedExam.getExam().getId());
        List<Long> correctQuestionIds = new ArrayList<>();

        for (Question question : examQuestions) {
            Long selectedAnswerId = submissionDto.getAnswers().get(question.getId());
            if (selectedAnswerId != null) {
                Answer selectedAnswer = answerRepository.findById(selectedAnswerId).orElse(null);
                if (selectedAnswer != null && selectedAnswer.getIsCorrect()) {
                    correctQuestionIds.add(question.getId());
                }
            }
        }

        result.setCorrectQuestionIds(correctQuestionIds);
        result.setTotalQuestions(examQuestions.size());
        result.calculateScore();

        Result savedResult = resultRepository.save(result);

        // Update assigned exam status
        assignedExam.setStatus(ExamStatus.COMPLETED);
        assignedExamRepository.save(assignedExam);

        // Send result notification email
        try {
            emailService.sendResultNotificationEmail(savedResult);
        } catch (Exception e) {
            System.err.println("Failed to send result notification email: " + e.getMessage());
        }

        return resultMapper.toResultResponseDto(savedResult);
    }

    public ResultResponseDto getResultById(Long id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
        return resultMapper.toResultResponseDto(result);
    }

    public List<ResultResponseDto> getResultsByStudent(Long studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        return results.stream()
                .map(resultMapper::toResultResponseDto)
                .collect(Collectors.toList());
    }

    public List<ResultResponseDto> getResultsByExam(Long examId) {
        List<Result> results = resultRepository.findByExamId(examId);
        return results.stream()
                .map(resultMapper::toResultResponseDto)
                .collect(Collectors.toList());
    }

    public List<ResultResponseDto> getResultsByProfessor(Long professorId) {
        List<Result> results = resultRepository.findByProfessorId(professorId);
        return results.stream()
                .map(resultMapper::toResultResponseDto)
                .collect(Collectors.toList());
    }

    public ResultResponseDto getResultByStudentAndExam(Long studentId, Long examId) {
        Result result = resultRepository.findByStudentIdAndExamId(studentId, examId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
        return resultMapper.toResultResponseDto(result);
    }

    public List<ResultResponseDto> getResultsByStatus(ResultStatus status) {
        List<Result> results = resultRepository.findByStatus(status);
        return results.stream()
                .map(resultMapper::toResultResponseDto)
                .collect(Collectors.toList());
    }

    public ExamStatisticsDto getExamStatistics(Long examId) {
        Double averageScore = resultRepository.findAverageScoreByExamId(examId);
        Long passedCount = resultRepository.countPassedByExamId(examId);
        Long failedCount = resultRepository.countFailedByExamId(examId);
        Long totalAttempts = passedCount + failedCount;

        return new ExamStatisticsDto(
                examId,
                averageScore != null ? averageScore : 0.0,
                passedCount,
                failedCount,
                totalAttempts,
                totalAttempts > 0 ? (passedCount.doubleValue() / totalAttempts) * 100 : 0.0
        );
    }

    @Transactional
    public void deleteResult(Long id, Long professorId) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));

        if (!result.getExam().getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("You can only delete results from your own exams");
        }

        resultRepository.delete(result);
    }

    public List<ExamStatisticsDto> getAllExamStatistics(Long professorId) {
        List<Exam> exams = examRepository.findByProfessorId(professorId);
        return exams.stream()
                .map(exam -> getExamStatistics(exam.getId()))
                .collect(Collectors.toList());
    }

    // Inner class for statistics
    public static class ExamStatisticsDto {
        private Long examId;
        private Double averageScore;
        private Long passedCount;
        private Long failedCount;
        private Long totalAttempts;
        private Double passRate;

        public ExamStatisticsDto(Long examId, Double averageScore, Long passedCount,
                                 Long failedCount, Long totalAttempts, Double passRate) {
            this.examId = examId;
            this.averageScore = averageScore;
            this.passedCount = passedCount;
            this.failedCount = failedCount;
            this.totalAttempts = totalAttempts;
            this.passRate = passRate;
        }

        // Getters and setters
        public Long getExamId() { return examId; }
        public void setExamId(Long examId) { this.examId = examId; }
        public Double getAverageScore() { return averageScore; }
        public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
        public Long getPassedCount() { return passedCount; }
        public void setPassedCount(Long passedCount) { this.passedCount = passedCount; }
        public Long getFailedCount() { return failedCount; }
        public void setFailedCount(Long failedCount) { this.failedCount = failedCount; }
        public Long getTotalAttempts() { return totalAttempts; }
        public void setTotalAttempts(Long totalAttempts) { this.totalAttempts = totalAttempts; }
        public Double getPassRate() { return passRate; }
        public void setPassRate(Double passRate) { this.passRate = passRate; }
    }
}