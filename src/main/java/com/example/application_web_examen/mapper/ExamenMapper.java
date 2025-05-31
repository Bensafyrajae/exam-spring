package com.example.application_web_examen.mapper;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {QuestionMapper.class, UserMapper.class})
public interface ExamenMapper {

    ExamenResponseDto toExamenResponseDto(Examen examen);

    Examen toExamenEntity(ExamenRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Examen partialUpdateExamen(ExamenRequestDto examenRequestDto, @MappingTarget Examen examen);
}