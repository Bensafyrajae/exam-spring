package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.TypeQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String texteQuestion;
    private Integer dureeEnSecondes;
    private TypeQuestion type;
    private List<String> options;
    private MediaResponseDto image;
}