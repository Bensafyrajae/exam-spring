package com.example.application_web_examen.repository;

import com.example.application_web_examen.enums.EmailType;
import com.example.application_web_examen.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    List<EmailNotification> findByRecipientEmail(String email);
    List<EmailNotification> findByType(EmailType type);
    List<EmailNotification> findBySentFalse();
}