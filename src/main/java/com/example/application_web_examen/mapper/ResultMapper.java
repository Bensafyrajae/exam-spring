package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.response.ResultResponseDto;
import com.example.application_web_examen.model.Result;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ExamMapper.class})
public interface ResultMapper {

    @Mapping(source = "percentage", target = "percentage")
    ResultResponseDto toResultResponseDto(Result result);
}