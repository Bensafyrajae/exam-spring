package com.example.application_web_examen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    private Integer questionsCount;
    private Integer durationMinutes;
    private Integer maxAttemptsAllowed = 1;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private LocalDateTime createdAt;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<AssignedExam> assignedExams = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Result> results = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.questionsCount == null && this.questions != null) {
            this.questionsCount = this.questions.size();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (this.questions != null) {
            this.questionsCount = this.questions.size();
        }
    }
}