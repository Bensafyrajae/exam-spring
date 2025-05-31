package com.example.application_web_examen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDto {
    private String name;
    private String description;
    private Integer durationMinutes;
    private Integer maxAttemptsAllowed;
    private List<QuestionRequestDto> questions;
}