package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private String passcode;
    private String uniqueLink;
    private LocalDateTime sentAt;
    private LocalDateTime finalDate;
    private Integer attemptsMade = 0;

    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.PENDING;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
        this.passcode = generatePasscode();
        this.uniqueLink = generateUniqueLink();
    }

    private String generatePasscode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    private String generateUniqueLink() {
        return UUID.randomUUID().toString();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.finalDate);
    }

    public boolean canTakeExam() {
        return this.status == ExamStatus.PENDING &&
                !isExpired() &&
                this.attemptsMade < this.exam.getMaxAttemptsAllowed();
    }
}