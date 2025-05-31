package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedExamResponseDto {
    private Long id;
    private StudentResponseDto student;
    private ExamResponseDto exam;
    private String passcode;
    private String uniqueLink;
    private LocalDateTime sentAt;
    private LocalDateTime finalDate;
    private Integer attemptsMade;
    private ExamStatus status;
    private Boolean canTakeExam;
    private Boolean isExpired;
}