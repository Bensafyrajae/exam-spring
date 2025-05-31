package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.AnswerRequestDto;
import com.example.application_web_examen.dto.response.AnswerResponseDto;
import com.example.application_web_examen.model.Answer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnswerMapper {

    AnswerResponseDto toAnswerResponseDto(Answer answer);

    @Mapping(target = "question", ignore = true)
    Answer toAnswerEntity(AnswerRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "question", ignore = true)
    Answer partialUpdateAnswer(AnswerRequestDto answerRequestDto, @MappingTarget Answer answer);
}