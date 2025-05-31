package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.response.ScoreResponseDto;
import com.example.application_web_examen.repository.ReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private final ReponseRepository reponseRepository;

    @Autowired
    public ScoreService(ReponseRepository reponseRepository) {
        this.reponseRepository = reponseRepository;
    }

    public ScoreResponseDto calculateScore(Long etudiantId, Long examenId) {
        Long correctCount = reponseRepository.countCorrectReponsesByEtudiantAndExamen(etudiantId, examenId);
        Long totalCount = reponseRepository.countTotalReponsesByEtudiantAndExamen(etudiantId, examenId);

        ScoreResponseDto scoreDto = new ScoreResponseDto();
        scoreDto.setNombreReponseCorrectes(correctCount);
        scoreDto.setNombreQuestions(totalCount);

        if (totalCount > 0) {
            double pourcentage = (double) correctCount / totalCount * 100;
            scoreDto.setPourcentage(pourcentage);
        } else {
            scoreDto.setPourcentage(0.0);
        }

        return scoreDto;
    }
}