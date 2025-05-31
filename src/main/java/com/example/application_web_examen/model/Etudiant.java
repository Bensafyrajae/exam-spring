package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class Etudiant extends User {


    @OneToMany(mappedBy = "etudiant")
    private List<Reponse> reponses = new ArrayList<>();

    @ManyToMany
    private List<Classe> classes = new ArrayList<>();

    public Etudiant() {
        this.setRole(Role.ETUDIANT);
    }

}
