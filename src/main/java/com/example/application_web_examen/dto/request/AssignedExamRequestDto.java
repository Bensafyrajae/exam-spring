package com.example.application_web_examen.dto.request;

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
public class AssignedExamRequestDto {
    private Long examId;
    private List<Long> studentIds;
    private LocalDateTime finalDate;
}