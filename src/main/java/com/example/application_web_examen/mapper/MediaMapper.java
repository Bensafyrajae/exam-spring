package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.response.MediaResponseDto;
import com.example.application_web_examen.model.Media;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {

    MediaResponseDto toMediaResponseDto(Media media);
}