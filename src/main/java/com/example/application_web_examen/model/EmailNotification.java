package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.EmailType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;
    private String subject;

    @Column(length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    private LocalDateTime sentAt;
    private Boolean sent = false;
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }
}