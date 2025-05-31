package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> correctAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> wrongAnswers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "question_image_urls")
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private QuestionType type = QuestionType.MCQ;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    public List<Answer> getAllAnswers() {
        List<Answer> allAnswers = new ArrayList<>();
        if (correctAnswers != null) allAnswers.addAll(correctAnswers);
        if (wrongAnswers != null) allAnswers.addAll(wrongAnswers);
        return allAnswers;
    }

    public Answer getCorrectAnswer() {
        return correctAnswers != null && !correctAnswers.isEmpty() ? correctAnswers.get(0) : null;
    }
}