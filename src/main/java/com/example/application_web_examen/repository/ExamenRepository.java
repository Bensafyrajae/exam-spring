package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {
    Optional<Examen> findByLienUnique(String lienUnique);

    // Recherche des examens créés par un professeur
    List<Examen> findByCreateurId(Long profId);
}