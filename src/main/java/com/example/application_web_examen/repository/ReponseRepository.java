package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
    List<Reponse> findByEtudiantIdAndQuestionExamenId(Long etudiantId, Long examenId);

    @Query("SELECT COUNT(r) FROM Reponse r WHERE r.etudiant.id = ?1 AND r.question.examen.id = ?2 AND r.estCorrect = true")
    Long countCorrectReponsesByEtudiantAndExamen(Long etudiantId, Long examenId);

    @Query("SELECT COUNT(r) FROM Reponse r WHERE r.etudiant.id = ?1 AND r.question.examen.id = ?2")
    Long countTotalReponsesByEtudiantAndExamen(Long etudiantId, Long examenId);
}