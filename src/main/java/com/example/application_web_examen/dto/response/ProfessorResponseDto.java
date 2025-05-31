package com.example.application_web_examen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponseDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private MediaResponseDto userPhoto;
}