package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.ReponseRequestDto;
import com.example.application_web_examen.dto.response.ReponseResponseDto;
import com.example.application_web_examen.dto.response.ScoreResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.ReponseMapper;
import com.example.application_web_examen.model.Etudiant;
import com.example.application_web_examen.model.Question;
import com.example.application_web_examen.model.Reponse;
import com.example.application_web_examen.repository.EtudiantRepository;
import com.example.application_web_examen.repository.QuestionRepository;
import com.example.application_web_examen.repository.ReponseRepository;
import com.example.application_web_examen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReponseService {

    private final ReponseRepository reponseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ReponseMapper reponseMapper;

    @Autowired
    public ReponseService(ReponseRepository reponseRepository,
                          QuestionRepository questionRepository,
                          UserRepository userRepository,
                          ReponseMapper reponseMapper) {
        this.reponseRepository = reponseRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.reponseMapper = reponseMapper;
    }

    @Transactional
    public ReponseResponseDto enregistrerReponse(ReponseRequestDto reponseDto) {
        Question question = questionRepository.findById(reponseDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question non trouvée"));

        Etudiant etudiant = (Etudiant) userRepository.findByEmail(reponseDto.getEmailEtudiant())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        Reponse reponse = reponseMapper.toReponseEntity(reponseDto);
        reponse.setQuestion(question);
        reponse.setEtudiant(etudiant);

        // Vérifier si la réponse est correcte
        boolean estCorrect = question.getBonneReponse().equalsIgnoreCase(reponseDto.getReponseTexte());
        reponse.setEstCorrect(estCorrect);

        Reponse savedReponse = reponseRepository.save(reponse);
        return reponseMapper.toReponseResponseDto(savedReponse);
    }

    public List<ReponseResponseDto> getReponsesByEtudiantAndExamen(Long etudiantId, Long examenId) {
        List<Reponse> reponses = reponseRepository.findByEtudiantIdAndQuestionExamenId(etudiantId, examenId);
        return reponses.stream()
                .map(reponseMapper::toReponseResponseDto)
                .collect(Collectors.toList());
    }

//    public ScoreResponseDto calculateScore(Long etudiantId, Long examenId) {
//        Long correctCount = reponseRepository.countCorrectReponsesByEtudiantAndExamen(etudiantId, examenId);
//        Long totalCount = reponseRepository.countTotalReponsesByEtudiantAndExamen(etudiantId, examenId);
//
//        ScoreResponseDto scoreDto = new ScoreResponseDto();
//        scoreDto.setNombreReponseCorrectes(correctCount);
//        scoreDto.setNombreQuestions(totalCount);
//
//        if (totalCount > 0)
}