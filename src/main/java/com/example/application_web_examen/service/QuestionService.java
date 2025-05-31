package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.QuestionRequestDto;
import com.example.application_web_examen.dto.response.QuestionResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.QuestionMapper;
import com.example.application_web_examen.model.Examen;
import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.model.Question;
import com.example.application_web_examen.repository.ExamenRepository;
import com.example.application_web_examen.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamenRepository examenRepository;
    private final QuestionMapper questionMapper;
    private final MediaService mediaService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           ExamenRepository examenRepository,
                           QuestionMapper questionMapper,
                           MediaService mediaService) {
        this.questionRepository = questionRepository;
        this.examenRepository = examenRepository;
        this.questionMapper = questionMapper;
        this.mediaService = mediaService;
    }

    @Transactional
    public QuestionResponseDto addQuestionToExamen(QuestionRequestDto questionDto, Long examenId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new ResourceNotFoundException("Examen non trouvé"));

        Question question = questionMapper.toQuestionEntity(questionDto);
        question.setExamen(examen);

        // Traitement de l'image si présente
//        if (questionDto.getImage() != null && !questionDto.getImage().isEmpty()) {
//            try {
//                Media media = mediaService.saveImage(questionDto.getImage());
//                question.setImage(media);
//            } catch (IOException e) {
//                throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
//            }
//        }

        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toQuestionResponseDto(savedQuestion);
    }

    public List<QuestionResponseDto> getQuestionsByExamenId(Long examenId) {
        List<Question> questions = questionRepository.findByExamenId(examenId);
        return questions.stream()
                .map(questionMapper::toQuestionResponseDto)
                .collect(Collectors.toList());
    }

    public QuestionResponseDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question non trouvée"));
        return questionMapper.toQuestionResponseDto(question);
    }

    @Transactional
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto questionDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question non trouvée"));

        questionMapper.partialUpdateQuestion(questionDto, question);

        // Traitement de l'image si présente
//        if (questionDto.getImage() != null && !questionDto.getImage().isEmpty()) {
//            try {
//                Media media = mediaService.saveImage(questionDto.getImage());
//                question.setImage(media);
//            } catch (IOException e) {
//                throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
//            }
//        }

        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toQuestionResponseDto(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}