package com.example.application_web_examen.service;

import com.example.application_web_examen.model.AssignedExam;
import com.example.application_web_examen.model.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    // En mémoire pour ce service simple - dans un vrai projet, utilisez Redis ou une base de données
    private final Map<Long, List<Notification>> userNotifications = new ConcurrentHashMap<>();

    public void createNotificationForExamAssignment(AssignedExam assignedExam) {
        Notification notification = new Notification(
                "EXAM_ASSIGNED",
                "New exam assigned: " + assignedExam.getExam().getName(),
                "You have been assigned a new exam. Deadline: " + assignedExam.getFinalDate(),
                false
        );

        addNotificationForUser(assignedExam.getStudent().getId(), notification);
    }

    public void createNotificationForResultAvailable(Result result) {
        Notification notification = new Notification(
                "RESULT_AVAILABLE",
                "Exam result available: " + result.getExam().getName(),
                "Your exam result is now available. Score: " + result.getPercentage() + "%",
                false
        );

        addNotificationForUser(result.getStudent().getId(), notification);
    }

    public void createNotificationForExamReminder(AssignedExam assignedExam) {
        Notification notification = new Notification(
                "EXAM_REMINDER",
                "Exam deadline approaching: " + assignedExam.getExam().getName(),
                "Don't forget to complete your exam before " + assignedExam.getFinalDate(),
                false
        );

        addNotificationForUser(assignedExam.getStudent().getId(), notification);
    }

    public void createNotificationForExamExpired(AssignedExam assignedExam) {
        Notification notification = new Notification(
                "EXAM_EXPIRED",
                "Exam expired: " + assignedExam.getExam().getName(),
                "The deadline for this exam has passed.",
                false
        );

        addNotificationForUser(assignedExam.getStudent().getId(), notification);
    }

    private void addNotificationForUser(Long userId, Notification notification) {
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }

    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(n -> !n.isRead())
                .toList();
    }

    public void markNotificationAsRead(Long userId, int notificationIndex) {
        List<Notification> notifications = userNotifications.get(userId);
        if (notifications != null && notificationIndex < notifications.size()) {
            notifications.get(notificationIndex).setRead(true);
        }
    }

    public void markAllAsReadForUser(Long userId) {
        List<Notification> notifications = userNotifications.get(userId);
        if (notifications != null) {
            notifications.forEach(n -> n.setRead(true));
        }
    }

    public int getUnreadCountForUser(Long userId) {
        return getUnreadNotificationsForUser(userId).size();
    }

    public void clearNotificationsForUser(Long userId) {
        userNotifications.remove(userId);
    }

    // Classe interne pour les notifications
    public static class Notification {
        private String type;
        private String title;
        private String message;
        private boolean read;
        private long timestamp;

        public Notification(String type, String title, String message, boolean read) {
            this.type = type;
            this.title = title;
            this.message = message;
            this.read = read;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters et setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}