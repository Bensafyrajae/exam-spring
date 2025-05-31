package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationResponseDto {
    private Long id;
    private String recipientEmail;
    private String subject;
    private EmailType type;
    private LocalDateTime sentAt;
    private Boolean sent;
    private String errorMessage;
}