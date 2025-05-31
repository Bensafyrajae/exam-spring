package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    List<Answer> findByQuestionIdAndIsCorrectTrue(Long questionId);
    List<Answer> findByQuestionIdAndIsCorrectFalse(Long questionId);
}