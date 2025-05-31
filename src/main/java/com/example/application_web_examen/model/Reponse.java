package com.example.application_web_examen.model;

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
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reponseTexte;
    private Boolean estCorrect;
    private LocalDateTime dateReponse;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Etudiant etudiant;

    @PrePersist
    public void prePersist() {
        this.dateReponse = LocalDateTime.now();
    }
}