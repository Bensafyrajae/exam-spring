package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.ExamenRequestDto;
import com.example.application_web_examen.dto.response.ExamenResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.ExamenMapper;
import com.example.application_web_examen.model.Examen;
import com.example.application_web_examen.model.Prof;
import com.example.application_web_examen.repository.ExamenRepository;
import com.example.application_web_examen.repository.ProfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final ProfRepository profRepository;
    private final ExamenMapper examenMapper;
    private final QuestionService questionService;

    @Autowired
    public ExamenService(ExamenRepository examenRepository,
                         ProfRepository profRepository,
                         ExamenMapper examenMapper,
                         QuestionService questionService) {
        this.examenRepository = examenRepository;
        this.profRepository = profRepository;
        this.examenMapper = examenMapper;
        this.questionService = questionService;
    }

    @Transactional
    public ExamenResponseDto createExamen(ExamenRequestDto examenDto, Long profId) {
        Prof prof = profRepository.findById(profId)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvé"));

        Examen examen = examenMapper.toExamenEntity(examenDto);
        examen.setCreateur(prof);

        Examen savedExamen = examenRepository.save(examen);

        // Traitement des questions
        if (examenDto.getQuestions() != null && !examenDto.getQuestions().isEmpty()) {
            examenDto.getQuestions().forEach(questionDto ->
                    questionService.addQuestionToExamen(questionDto, savedExamen.getId()));
        }

        return examenMapper.toExamenResponseDto(
                examenRepository.findById(savedExamen.getId()).orElseThrow()
        );
    }

    public ExamenResponseDto getExamenById(Long id) {
        Examen examen = examenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen non trouvé"));
        return examenMapper.toExamenResponseDto(examen);
    }

    public ExamenResponseDto getExamenByLienUnique(String lienUnique) {
        Examen examen = examenRepository.findByLienUnique(lienUnique)
                .orElseThrow(() -> new ResourceNotFoundException("Examen non trouvé"));
        return examenMapper.toExamenResponseDto(examen);
    }

    public List<ExamenResponseDto> getExamensByProf(Long profId) {
        List<Examen> examens = examenRepository.findByCreateurId(profId);
        return examens.stream()
                .map(examenMapper::toExamenResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteExamen(Long id) {
        examenRepository.deleteById(id);
    }
}