package com.example.application_web_examen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String lienUnique;
    private LocalDateTime dateCreation;

    @ManyToOne
    private Prof createur;

    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.lienUnique = UUID.randomUUID().toString();
        this.dateCreation = LocalDateTime.now();
    }
}