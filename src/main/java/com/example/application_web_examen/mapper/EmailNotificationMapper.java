package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.response.EmailNotificationResponseDto;
import com.example.application_web_examen.model.EmailNotification;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailNotificationMapper {

    EmailNotificationResponseDto toEmailNotificationResponseDto(EmailNotification emailNotification);
}