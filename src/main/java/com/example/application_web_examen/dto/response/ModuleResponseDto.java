package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleResponseDto {
    private Long id;
    private String nom;
    private String code;
    private String description;
}