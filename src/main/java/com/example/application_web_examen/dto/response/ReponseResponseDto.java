package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReponseResponseDto {
    private Long id;
    private String reponseTexte;
    private Boolean estCorrect;
    private LocalDateTime dateReponse;
    private Long questionId;
    private String emailEtudiant;
}