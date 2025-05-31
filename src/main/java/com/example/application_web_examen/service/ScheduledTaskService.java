package com.example.application_web_examen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    private final AssignedExamService assignedExamService;
    private final EmailService emailService;

    @Autowired
    public ScheduledTaskService(AssignedExamService assignedExamService, EmailService emailService) {
        this.assignedExamService = assignedExamService;
        this.emailService = emailService;
    }

    // Exécute toutes les minutes pour vérifier les examens expirés
    @Scheduled(fixedRate = 60000)
    public void updateExpiredExams() {
        try {
            assignedExamService.updateExpiredExams();
        } catch (Exception e) {
            System.err.println("Error updating expired exams: " + e.getMessage());
        }
    }

    // Exécute toutes les heures pour envoyer des rappels
    @Scheduled(fixedRate = 3600000)
    public void sendExamReminders() {
        try {
            assignedExamService.sendReminders();
        } catch (Exception e) {
            System.err.println("Error sending reminders: " + e.getMessage());
        }
    }

    // Exécute tous les jours à 02:00 pour réessayer les emails échoués
    @Scheduled(cron = "0 0 2 * * ?")
    public void retryFailedEmails() {
        try {
            emailService.retryFailedEmails();
        } catch (Exception e) {
            System.err.println("Error retrying failed emails: " + e.getMessage());
        }
    }

    // Nettoie les notifications email anciennes (plus de 30 jours)
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupOldNotifications() {
        try {
            // Cette méthode devrait être implémentée dans EmailService
            // emailService.cleanupOldNotifications();
        } catch (Exception e) {
            System.err.println("Error cleaning up old notifications: " + e.getMessage());
        }
    }
}