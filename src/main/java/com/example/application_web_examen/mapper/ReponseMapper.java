package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.ReponseRequestDto;
import com.example.application_web_examen.dto.response.ReponseResponseDto;
import com.example.application_web_examen.model.Reponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReponseMapper {

    @Mapping(source = "etudiant.email", target = "emailEtudiant")
    @Mapping(source = "question.id", target = "questionId")
    ReponseResponseDto toReponseResponseDto(Reponse reponse);

    // La question et l'étudiant sont gérés séparément
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "etudiant", ignore = true)
    Reponse toReponseEntity(ReponseRequestDto dto);
}