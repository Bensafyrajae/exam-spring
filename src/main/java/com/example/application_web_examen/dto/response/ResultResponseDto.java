package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponseDto {
    private Long id;
    private ExamResponseDto exam;
    private StudentResponseDto student;
    private Double score;
    private ResultStatus status;
    private LocalDateTime date;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Double percentage;
    private Map<Long, Long> studentAnswers; // QuestionId -> AnswerId
}