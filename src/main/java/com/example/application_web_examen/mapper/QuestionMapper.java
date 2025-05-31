package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.QuestionRequestDto;
import com.example.application_web_examen.dto.response.QuestionResponseDto;
import com.example.application_web_examen.model.Question;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {MediaMapper.class})
public interface QuestionMapper {

    QuestionResponseDto toQuestionResponseDto(Question question);

    // L'image est gérée séparément car c'est un MultipartFile
    @Mapping(target = "image", ignore = true)
    Question toQuestionEntity(QuestionRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "image", ignore = true)
    Question partialUpdateQuestion(QuestionRequestDto questionRequestDto, @MappingTarget Question question);
}