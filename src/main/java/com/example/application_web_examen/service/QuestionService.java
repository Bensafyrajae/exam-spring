package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.QuestionRequestDto;
import com.example.application_web_examen.dto.response.QuestionResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.QuestionMapper;
import com.example.application_web_examen.model.Answer;
import com.example.application_web_examen.model.Exam;
import com.example.application_web_examen.model.Question;
import com.example.application_web_examen.repository.ExamRepository;
import com.example.application_web_examen.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final QuestionMapper questionMapper;
    private final MediaService mediaService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           ExamRepository examRepository,
                           QuestionMapper questionMapper,
                           MediaService mediaService) {
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
        this.questionMapper = questionMapper;
        this.mediaService = mediaService;
    }

    @Transactional
    public QuestionResponseDto addQuestionToExam(QuestionRequestDto questionDto, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Question question = questionMapper.toQuestionEntity(questionDto);
        question.setExam(exam);

        // Process answers
        List<Answer> correctAnswers = new ArrayList<>();
        List<Answer> wrongAnswers = new ArrayList<>();

        if (questionDto.getCorrectAnswers() != null) {
            for (String answerText : questionDto.getCorrectAnswers()) {
                Answer answer = new Answer(answerText, true);
                answer.setQuestion(question);
                correctAnswers.add(answer);
            }
        }

        if (questionDto.getWrongAnswers() != null) {
            for (String answerText : questionDto.getWrongAnswers()) {
                Answer answer = new Answer(answerText, false);
                answer.setQuestion(question);
                wrongAnswers.add(answer);
            }
        }

        question.setCorrectAnswers(correctAnswers);
        question.setWrongAnswers(wrongAnswers);

        // Process images if provided
        List<String> imageUrls = new ArrayList<>();
        if (questionDto.getImages() != null && !questionDto.getImages().isEmpty()) {
            for (var image : questionDto.getImages()) {
                try {
                    String imageUrl = mediaService.saveQuestionImage(image);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Error saving image", e);
                }
            }
        }
        question.setImageUrls(imageUrls);

        Question savedQuestion = questionRepository.save(question);

        // Update exam questions count
        exam.setQuestionsCount(questionRepository.countByExamId(examId));
        examRepository.save(exam);

        return questionMapper.toQuestionResponseDto(savedQuestion);
    }

    public List<QuestionResponseDto> getQuestionsByExamId(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        return questions.stream()
                .map(questionMapper::toQuestionResponseDto)
                .collect(Collectors.toList());
    }

    public QuestionResponseDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return questionMapper.toQuestionResponseDto(question);
    }

    @Transactional
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto questionDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        questionMapper.partialUpdateQuestion(questionDto, question);

        // Update answers if provided
        if (questionDto.getCorrectAnswers() != null || questionDto.getWrongAnswers() != null) {
            question.getCorrectAnswers().clear();
            question.getWrongAnswers().clear();

            if (questionDto.getCorrectAnswers() != null) {
                for (String answerText : questionDto.getCorrectAnswers()) {
                    Answer answer = new Answer(answerText, true);
                    answer.setQuestion(question);
                    question.getCorrectAnswers().add(answer);
                }
            }

            if (questionDto.getWrongAnswers() != null) {
                for (String answerText : questionDto.getWrongAnswers()) {
                    Answer answer = new Answer(answerText, false);
                    answer.setQuestion(question);
                    question.getWrongAnswers().add(answer);
                }
            }
        }

        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toQuestionResponseDto(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Long examId = question.getExam().getId();
        questionRepository.deleteById(id);

        // Update exam questions count
        Exam exam = examRepository.findById(examId).orElseThrow();
        exam.setQuestionsCount(questionRepository.countByExamId(examId));
        examRepository.save(exam);
    }

    public int getQuestionCountByExamId(Long examId) {
        return questionRepository.countByExamId(examId);
    }

    public List<QuestionResponseDto> getQuestionsForExamTaking(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        return questions.stream()
                .map(question -> {
                    QuestionResponseDto dto = questionMapper.toQuestionResponseDto(question);
                    // Remove correct answer indication for students
                    dto.getAnswers().forEach(answer -> answer.setIsCorrect(null));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}