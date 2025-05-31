package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer questionsCount;
    private Integer durationMinutes;
    private Integer maxAttemptsAllowed;
    private ProfessorResponseDto professor;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private List<QuestionResponseDto> questions;
}