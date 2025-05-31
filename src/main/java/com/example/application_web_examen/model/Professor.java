package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Professor extends User {

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "professor")
    private List<AssignedExam> assignedExams = new ArrayList<>();

    public Professor() {
        this.setRole(Role.PROFESSOR);
    }
}