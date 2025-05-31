package com.example.application_web_examen.model;

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
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Integer anneeAcademique;

    @ManyToMany
    @JoinTable(
            name = "classe_module",
            joinColumns = @JoinColumn(name = "classe_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<Module> modules = new ArrayList<>();

    @ManyToMany(mappedBy = "classes")
    private List<Etudiant> etudiants = new ArrayList<>();

    @OneToMany(mappedBy = "classe")
    private List<ExamenClasse> examenClasses = new ArrayList<>();
}