package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.ExamRequestDto;
import com.example.application_web_examen.dto.response.ExamResponseDto;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping
    public ResponseEntity<ExamResponseDto> createExam(
            @RequestBody ExamRequestDto examDto,
            @AuthenticationPrincipal User user) {
        ExamResponseDto response = examService.createExam(examDto, user.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<ExamResponseDto>> getMyExams(@AuthenticationPrincipal User user) {
        List<ExamResponseDto> exams = examService.getExamsByProfessor(user.getId());
        return ResponseEntity.ok(exams);
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDto> getExamById(@PathVariable Long id) {
        ExamResponseDto exam = examService.getExamById(id);
        return ResponseEntity.ok(exam);
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDto> updateExam(
            @PathVariable Long id,
            @RequestBody ExamRequestDto examDto,
            @AuthenticationPrincipal User user) {
        ExamResponseDto response = examService.updateExam(id, examDto, user.getId());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        examService.deleteExam(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleExamStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        examService.toggleExamStatus(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ExamResponseDto>> getAllActiveExams() {
        List<ExamResponseDto> exams = examService.getAllActiveExams();
        return ResponseEntity.ok(exams);
    }
}