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
public class ExamenResponseDto {
    private Long id;
    private String nom;
    private String description;
    private String lienUnique;
    private LocalDateTime dateCreation;
    private ProfResponseDto createur;
    private List<QuestionResponseDto> questions;
}