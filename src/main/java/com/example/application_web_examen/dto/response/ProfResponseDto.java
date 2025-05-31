package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.Specialty;
import com.example.application_web_examen.model.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfResponseDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private Specialty specialty;
    private String location;
    private int experience;
    private Media userPhoto;
}
