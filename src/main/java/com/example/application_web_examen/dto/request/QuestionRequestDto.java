package com.example.application_web_examen.dto.request;

import com.example.application_web_examen.enums.QuestionType;
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
    private String questionText;
    private QuestionType type;
    private List<String> correctAnswers;
    private List<String> wrongAnswers;
    private List<MultipartFile> images;
}