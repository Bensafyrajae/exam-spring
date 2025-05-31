package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.AnswerRequestDto;
import com.example.application_web_examen.dto.response.AnswerResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.AnswerMapper;
import com.example.application_web_examen.model.Answer;
import com.example.application_web_examen.model.Question;
import com.example.application_web_examen.repository.AnswerRepository;
import com.example.application_web_examen.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerMapper answerMapper;

    @Autowired
    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository,
                         AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.answerMapper = answerMapper;
    }

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto answerDto, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Answer answer = answerMapper.toAnswerEntity(answerDto);
        answer.setQuestion(question);

        Answer savedAnswer = answerRepository.save(answer);
        return answerMapper.toAnswerResponseDto(savedAnswer);
    }

    public List<AnswerResponseDto> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        return answers.stream()
                .map(answerMapper::toAnswerResponseDto)
                .collect(Collectors.toList());
    }

    public List<AnswerResponseDto> getCorrectAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionIdAndIsCorrectTrue(questionId);
        return answers.stream()
                .map(answerMapper::toAnswerResponseDto)
                .collect(Collectors.toList());
    }

    public List<AnswerResponseDto> getWrongAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionIdAndIsCorrectFalse(questionId);
        return answers.stream()
                .map(answerMapper::toAnswerResponseDto)
                .collect(Collectors.toList());
    }

    public AnswerResponseDto getAnswerById(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
        return answerMapper.toAnswerResponseDto(answer);
    }

    @Transactional
    public AnswerResponseDto updateAnswer(Long id, AnswerRequestDto answerDto) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        answerMapper.partialUpdateAnswer(answerDto, answer);
        Answer updatedAnswer = answerRepository.save(answer);
        return answerMapper.toAnswerResponseDto(updatedAnswer);
    }

    @Transactional
    public void deleteAnswer(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Answer not found");
        }
        answerRepository.deleteById(id);
    }

    public boolean isCorrectAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
        return answer.getIsCorrect();
    }

    @Transactional
    public void deleteAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        answerRepository.deleteAll(answers);
    }
}