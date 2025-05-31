package com.example.application_web_examen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRequestDto {
    private String nom;
    private String code;
    private String description;
}