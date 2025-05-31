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
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String code;
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<ModuleProfesseur> moduleProfesseurs = new ArrayList<>();

    @ManyToMany(mappedBy = "modules")
    private List<Classe> classes = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Examen> examens = new ArrayList<>();
}