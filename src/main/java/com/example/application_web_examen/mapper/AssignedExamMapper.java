package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.AssignedExamRequestDto;
import com.example.application_web_examen.dto.response.AssignedExamResponseDto;
import com.example.application_web_examen.model.AssignedExam;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ExamMapper.class})
public interface AssignedExamMapper {

    @Mapping(source = "canTakeExam", target = "canTakeExam")
    @Mapping(source = "expired", target = "isExpired")
    AssignedExamResponseDto toAssignedExamResponseDto(AssignedExam assignedExam);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "passcode", ignore = true)
    @Mapping(target = "uniqueLink", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "attemptsMade", ignore = true)
    @Mapping(target = "status", ignore = true)
    AssignedExam toAssignedExamEntity(AssignedExamRequestDto dto);
}