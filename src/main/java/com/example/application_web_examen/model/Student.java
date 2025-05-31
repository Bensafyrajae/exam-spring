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
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<AssignedExam> assignedExams = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Result> examsResults = new ArrayList<>();

    public Student() {
        this.setRole(Role.STUDENT);
    }
}