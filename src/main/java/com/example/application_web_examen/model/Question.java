package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.TypeQuestion;
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

    private String texteQuestion;
    private Integer dureeEnSecondes;

    @Enumerated(EnumType.STRING)
    private TypeQuestion type;

    // Pour les QCM
    @ElementCollection
    private List<String> options = new ArrayList<>();

    private String bonneReponse;

    @ManyToOne
    private Examen examen;

    @OneToOne(cascade = CascadeType.ALL)
    private Media image;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Reponse> reponses = new ArrayList<>();
}