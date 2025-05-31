package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.Role;
import com.example.application_web_examen.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Entity
public class Prof extends User {

    public Prof() {
        this.setRole(Role.PROF);
    }

    @OneToMany(mappedBy = "createur")
    private List<Examen> examens = new ArrayList<>();

    @OneToMany(mappedBy = "professeur")
    private List<ModuleProfesseur> moduleProfesseurs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    private String location;
    private int experience;

}
