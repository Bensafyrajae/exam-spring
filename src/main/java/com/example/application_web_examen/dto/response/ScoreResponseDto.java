package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDto {
    private Long nombreReponseCorrectes;
    private Long nombreQuestions;
    private Double pourcentage;
}