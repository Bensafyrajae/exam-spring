package com.example.application_web_examen.dto.request;

import com.example.application_web_examen.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfRequestDto {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Specialty specialty;
    private String location;
    private int experience;
}
