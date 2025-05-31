package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.ExamenRequestDto;
import com.example.application_web_examen.dto.response.ExamenResponseDto;
import com.example.application_web_examen.model.Examen;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {QuestionMapper.class, UserMapper.class})
public interface ExamenMapper {

    ExamenResponseDto toExamenResponseDto(Examen examen);

    Examen toExamenEntity(ExamenRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Examen partialUpdateExamen(ExamenRequestDto examenRequestDto, @MappingTarget Examen examen);
}