package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.email IN :emails")
    List<Student> findByEmailIn(List<String> emails);
}