package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.ExamRequestDto;
import com.example.application_web_examen.dto.response.ExamResponseDto;
import com.example.application_web_examen.model.Exam;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, QuestionMapper.class})
public interface ExamMapper {

    ExamResponseDto toExamResponseDto(Exam exam);

    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "results", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "questionsCount", ignore = true)
    Exam toExamEntity(ExamRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "results", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "questionsCount", ignore = true)
    Exam partialUpdateExam(ExamRequestDto examRequestDto, @MappingTarget Exam exam);
}