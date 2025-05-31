package com.example.application_web_examen.dto.request;

import com.example.application_web_examen.enums.TypeQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {
    private String texteQuestion;
    private Integer dureeEnSecondes;
    private TypeQuestion type;
    private List<String> options;
    private String bonneReponse;
    private MultipartFile image;
}