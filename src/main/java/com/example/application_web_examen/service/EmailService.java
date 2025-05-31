package com.example.application_web_examen.service;

import com.example.application_web_examen.enums.EmailType;
import com.example.application_web_examen.model.AssignedExam;
import com.example.application_web_examen.model.EmailNotification;
import com.example.application_web_examen.model.Result;
import com.example.application_web_examen.repository.EmailNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailNotificationRepository emailNotificationRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender, EmailNotificationRepository emailNotificationRepository) {
        this.mailSender = mailSender;
        this.emailNotificationRepository = emailNotificationRepository;
    }

    public void sendExamAssignmentEmail(AssignedExam assignedExam) {
        String subject = "New Exam Assignment: " + assignedExam.getExam().getName();
        String examLink = baseUrl + "/exam/take/" + assignedExam.getUniqueLink();

        String content = String.format(
                "Dear %s,\n\n" +
                        "You have been assigned a new exam: %s\n" +
                        "Professor: %s\n" +
                        "Duration: %d minutes\n" +
                        "Deadline: %s\n" +
                        "Passcode: %s\n\n" +
                        "Click here to take the exam: %s\n\n" +
                        "Best regards,\n" +
                        "Exam System",
                assignedExam.getStudent().getFullName(),
                assignedExam.getExam().getName(),
                assignedExam.getExam().getProfessor().getFullName(),
                assignedExam.getExam().getDurationMinutes(),
                assignedExam.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                assignedExam.getPasscode(),
                examLink
        );

        sendEmail(assignedExam.getStudent().getEmail(), subject, content, EmailType.EXAM_ASSIGNMENT);
    }

    public void sendResultNotificationEmail(Result result) {
        String subject = "Exam Result: " + result.getExam().getName();

        String content = String.format(
                "Dear %s,\n\n" +
                        "Your exam results are ready!\n\n" +
                        "Exam: %s\n" +
                        "Score: %.2f%%\n" +
                        "Status: %s\n" +
                        "Correct Answers: %d/%d\n" +
                        "Date Completed: %s\n\n" +
                        "Best regards,\n" +
                        "Exam System",
                result.getStudent().getFullName(),
                result.getExam().getName(),
                result.getPercentage(),
                result.getStatus(),
                result.getCorrectAnswers(),
                result.getTotalQuestions(),
                result.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        sendEmail(result.getStudent().getEmail(), subject, content, EmailType.RESULT_NOTIFICATION);
    }

    public void sendReminderEmail(AssignedExam assignedExam) {
        String subject = "Exam Reminder: " + assignedExam.getExam().getName();
        String examLink = baseUrl + "/exam/take/" + assignedExam.getUniqueLink();

        String content = String.format(
                "Dear %s,\n\n" +
                        "This is a reminder that you have an upcoming exam deadline.\n\n" +
                        "Exam: %s\n" +
                        "Deadline: %s\n" +
                        "Passcode: %s\n\n" +
                        "Click here to take the exam: %s\n\n" +
                        "Best regards,\n" +
                        "Exam System",
                assignedExam.getStudent().getFullName(),
                assignedExam.getExam().getName(),
                assignedExam.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                assignedExam.getPasscode(),
                examLink
        );

        sendEmail(assignedExam.getStudent().getEmail(), subject, content, EmailType.REMINDER);
    }

    private void sendEmail(String to, String subject, String content, EmailType type) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);

            // Log email notification
            EmailNotification notification = new EmailNotification();
            notification.setRecipientEmail(to);
            notification.setSubject(subject);
            notification.setContent(content);
            notification.setType(type);
            notification.setSent(true);
            emailNotificationRepository.save(notification);

        } catch (Exception e) {
            // Log failed email notification
            EmailNotification notification = new EmailNotification();
            notification.setRecipientEmail(to);
            notification.setSubject(subject);
            notification.setContent(content);
            notification.setType(type);
            notification.setSent(false);
            notification.setErrorMessage(e.getMessage());
            emailNotificationRepository.save(notification);
        }
    }
}