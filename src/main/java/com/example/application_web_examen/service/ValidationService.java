package com.example.application_web_examen.service;

import com.example.application_web_examen.model.AssignedExam;
import com.example.application_web_examen.model.Exam;
import com.example.application_web_examen.model.Question;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {

    public List<String> validateExam(Exam exam) {
        List<String> errors = new ArrayList<>();

        if (exam.getName() == null || exam.getName().trim().isEmpty()) {
            errors.add("Exam name is required");
        }

        if (exam.getDurationMinutes() == null || exam.getDurationMinutes() <= 0) {
            errors.add("Exam duration must be greater than 0");
        }

        if (exam.getMaxAttemptsAllowed() == null || exam.getMaxAttemptsAllowed() <= 0) {
            errors.add("Max attempts must be greater than 0");
        }

        if (exam.getQuestions() == null || exam.getQuestions().isEmpty()) {
            errors.add("Exam must have at least one question");
        } else {
            for (int i = 0; i < exam.getQuestions().size(); i++) {
                Question question = exam.getQuestions().get(i);
                List<String> questionErrors = validateQuestion(question, i + 1);
                errors.addAll(questionErrors);
            }
        }

        return errors;
    }

    public List<String> validateQuestion(Question question, int questionNumber) {
        List<String> errors = new ArrayList<>();
        String prefix = "Question " + questionNumber + ": ";

        if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
            errors.add(prefix + "Question text is required");
        }

        if (question.getType() == null) {
            errors.add(prefix + "Question type is required");
        }

        // Validation pour les QCM
        if (question.getType() != null && question.getType().name().equals("MCQ")) {
            if (question.getCorrectAnswers() == null || question.getCorrectAnswers().isEmpty()) {
                errors.add(prefix + "MCQ must have at least one correct answer");
            }

            if (question.getWrongAnswers() == null || question.getWrongAnswers().size() < 2) {
                errors.add(prefix + "MCQ must have at least 2 wrong answers");
            }

            int totalAnswers = 0;
            if (question.getCorrectAnswers() != null) totalAnswers += question.getCorrectAnswers().size();
            if (question.getWrongAnswers() != null) totalAnswers += question.getWrongAnswers().size();

            if (totalAnswers < 3) {
                errors.add(prefix + "MCQ must have at least 3 total answers");
            }
        }

        // Validation pour les réponses directes
        if (question.getType() != null && question.getType().name().equals("DIRECT_ANSWER")) {
            if (question.getCorrectAnswers() == null || question.getCorrectAnswers().isEmpty()) {
                errors.add(prefix + "Direct answer question must have a correct answer");
            }
        }

        return errors;
    }

    public List<String> validateExamAssignment(AssignedExam assignedExam) {
        List<String> errors = new ArrayList<>();

        if (assignedExam.getFinalDate() == null) {
            errors.add("Final date is required");
        } else if (assignedExam.getFinalDate().isBefore(LocalDateTime.now())) {
            errors.add("Final date cannot be in the past");
        }

        if (assignedExam.getExam() == null) {
            errors.add("Exam is required");
        } else if (!assignedExam.getExam().getIsActive()) {
            errors.add("Cannot assign inactive exam");
        }

        if (assignedExam.getStudent() == null) {
            errors.add("Student is required");
        }

        return errors;
    }

    public boolean canStudentTakeExam(AssignedExam assignedExam) {
        if (assignedExam == null) return false;
        if (assignedExam.isExpired()) return false;
        if (assignedExam.getAttemptsMade() >= assignedExam.getExam().getMaxAttemptsAllowed()) return false;
        if (!assignedExam.getExam().getIsActive()) return false;

        return assignedExam.canTakeExam();
    }

    public List<String> validateExamSubmission(Map<Long, Long> answers, List<Question> questions) {
        List<String> errors = new ArrayList<>();

        if (answers == null || answers.isEmpty()) {
            errors.add("At least one answer is required");
            return errors;
        }

        for (Question question : questions) {
            if (!answers.containsKey(question.getId())) {
                errors.add("Answer required for question: " + question.getQuestionText());
            }
        }

        return errors;
    }

    public boolean isValidEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Au moins une lettre et un chiffre
        return password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    public List<String> validateUserRegistration(String username, String email, String password, String fullName) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().length() < 3) {
            errors.add("Username must be at least 3 characters long");
        }

        if (fullName == null || fullName.trim().length() < 2) {
            errors.add("Full name must be at least 2 characters long");
        }

        if (!isValidEmailFormat(email)) {
            errors.add("Invalid email format");
        }

        if (!isValidPassword(password)) {
            errors.add("Password must be at least 6 characters and contain at least one letter and one number");
        }

        return errors;
    }
}