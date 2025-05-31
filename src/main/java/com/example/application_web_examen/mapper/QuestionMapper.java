package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.QuestionRequestDto;
import com.example.application_web_examen.dto.response.QuestionResponseDto;
import com.example.application_web_examen.model.Question;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {AnswerMapper.class})
public interface QuestionMapper {

    @Mapping(source = "allAnswers", target = "answers")
    QuestionResponseDto toQuestionResponseDto(Question question);

    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "correctAnswers", ignore = true)
    @Mapping(target = "wrongAnswers", ignore = true)
    @Mapping(target = "imageUrls", ignore = true)
    Question toQuestionEntity(QuestionRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "correctAnswers", ignore = true)
    @Mapping(target = "wrongAnswers", ignore = true)
    @Mapping(target = "imageUrls", ignore = true)
    Question partialUpdateQuestion(QuestionRequestDto questionRequestDto, @MappingTarget Question question);
}