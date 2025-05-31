package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClasseResponseDto {
    private Long id;
    private String nom;
    private Integer anneeAcademique;
    private List<ModuleResponseDto> modules;
}