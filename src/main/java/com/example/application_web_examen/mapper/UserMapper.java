package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.ProfessorRequestDto;
import com.example.application_web_examen.dto.request.StudentRequestDto;
import com.example.application_web_examen.dto.response.ProfessorResponseDto;
import com.example.application_web_examen.dto.response.StudentResponseDto;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.model.Student;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {MediaMapper.class})
public interface UserMapper {

    // Student mappings
    StudentResponseDto toStudentResponseDto(Student student);

    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "examsResults", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userPhoto", ignore = true)
    Student toStudentEntity(StudentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "examsResults", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userPhoto", ignore = true)
    Student partialUpdateStudent(StudentRequestDto studentRequestDto, @MappingTarget Student student);

    // Professor mappings
    ProfessorResponseDto toProfessorResponseDto(Professor professor);

    @Mapping(target = "exams", ignore = true)
    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userPhoto", ignore = true)
    Professor toProfessorEntity(ProfessorRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "exams", ignore = true)
    @Mapping(target = "assignedExams", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userPhoto", ignore = true)
    Professor partialUpdateProfessor(ProfessorRequestDto professorRequestDto, @MappingTarget Professor professor);
}