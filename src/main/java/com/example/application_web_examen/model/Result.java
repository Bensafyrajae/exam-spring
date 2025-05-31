package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.ResultStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ElementCollection
    @CollectionTable(name = "result_correct_questions")
    private List<Long> correctQuestionIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "result_student_answers")
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer_id")
    private Map<Long, Long> studentAnswers = new HashMap<>();

    private Double score;

    @Enumerated(EnumType.STRING)
    private ResultStatus status = ResultStatus.NOT_TAKEN;

    private LocalDateTime date;
    private Integer totalQuestions;
    private Integer correctAnswers;

    @PrePersist
    public void prePersist() {
        this.date = LocalDateTime.now();
    }

    public void calculateScore() {
        if (this.totalQuestions != null && this.totalQuestions > 0) {
            this.correctAnswers = this.correctQuestionIds.size();
            this.score = (double) this.correctAnswers / this.totalQuestions * 100;
            this.status = this.score >= 50.0 ? ResultStatus.PASSED : ResultStatus.FAILED;
        }
    }

    public double getPercentage() {
        return this.score != null ? this.score : 0.0;
    }
}