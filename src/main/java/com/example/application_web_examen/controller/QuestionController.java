package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.QuestionRequestDto;
import com.example.application_web_examen.dto.response.QuestionResponseDto;
import com.example.application_web_examen.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @PostMapping("/examen/{examenId}")
    public ResponseEntity<QuestionResponseDto> addQuestionToExamen(
            @RequestBody QuestionRequestDto questionRequestDto,
            @PathVariable Long examenId) {
        QuestionResponseDto createdQuestion = questionService.addQuestionToExamen(questionRequestDto, examenId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/examen/{examenId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByExamenId(@PathVariable Long examenId) {
        List<QuestionResponseDto> questions = questionService.getQuestionsByExamenId(examenId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long id) {
        QuestionResponseDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionRequestDto questionRequestDto) {
        QuestionResponseDto updatedQuestion = questionService.updateQuestion(id, questionRequestDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}