package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.ExamRequestDto;
import com.example.application_web_examen.dto.response.ExamResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.exception.UnauthorizedException;
import com.example.application_web_examen.mapper.ExamMapper;
import com.example.application_web_examen.model.Exam;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.repository.ExamRepository;
import com.example.application_web_examen.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ProfessorRepository professorRepository;
    private final ExamMapper examMapper;
    private final QuestionService questionService;

    @Autowired
    public ExamService(ExamRepository examRepository,
                       ProfessorRepository professorRepository,
                       ExamMapper examMapper,
                       QuestionService questionService) {
        this.examRepository = examRepository;
        this.professorRepository = professorRepository;
        this.examMapper = examMapper;
        this.questionService = questionService;
    }

    @Transactional
    public ExamResponseDto createExam(ExamRequestDto examDto, Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));

        Exam exam = examMapper.toExamEntity(examDto);
        exam.setProfessor(professor);
        exam.setCreatedAt(LocalDateTime.now());
        exam.setIsActive(true);

        Exam savedExam = examRepository.save(exam);

        // Add questions if provided
        if (examDto.getQuestions() != null && !examDto.getQuestions().isEmpty()) {
            examDto.getQuestions().forEach(questionDto ->
                    questionService.addQuestionToExam(questionDto, savedExam.getId()));
        }

        return examMapper.toExamResponseDto(
                examRepository.findById(savedExam.getId()).orElseThrow()
        );
    }

    public ExamResponseDto getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        return examMapper.toExamResponseDto(exam);
    }

    public List<ExamResponseDto> getExamsByProfessor(Long professorId) {
        List<Exam> exams = examRepository.findByProfessorId(professorId);
        return exams.stream()
                .map(examMapper::toExamResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExamResponseDto> getAllActiveExams() {
        List<Exam> exams = examRepository.findByIsActiveTrue();
        return exams.stream()
                .map(examMapper::toExamResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExamResponseDto> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream()
                .map(examMapper::toExamResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamResponseDto updateExam(Long id, ExamRequestDto examDto, Long professorId) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (!exam.getProfessor().getId().equals(professorId)) {
            throw new UnauthorizedException("You are not authorized to update this exam");
        }

        examMapper.partialUpdateExam(examDto, exam);
        Exam updatedExam = examRepository.save(exam);
        return examMapper.toExamResponseDto(updatedExam);
    }

    @Transactional
    public void deleteExam(Long id, Long professorId) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (!exam.getProfessor().getId().equals(professorId)) {
            throw new UnauthorizedException("You are not authorized to delete this exam");
        }

        examRepository.delete(exam);
    }

    @Transactional
    public void toggleExamStatus(Long id, Long professorId) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (!exam.getProfessor().getId().equals(professorId)) {
            throw new UnauthorizedException("You are not authorized to modify this exam");
        }

        exam.setIsActive(!exam.getIsActive());
        examRepository.save(exam);
    }

    public boolean isExamOwnedByProfessor(Long examId, Long professorId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        return exam.getProfessor().getId().equals(professorId);
    }

    public Long getExamDuration(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        return exam.getDurationMinutes().longValue();
    }
}