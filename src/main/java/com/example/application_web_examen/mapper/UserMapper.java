package com.example.application_web_examen.mapper;

import com.example.application_web_examen.dto.request.AdminRequestDto;
import com.example.application_web_examen.dto.request.EtudiantRequestDto;
import com.example.application_web_examen.dto.request.ProfRequestDto;
import com.example.application_web_examen.dto.response.AdminResponseDto;
import com.example.application_web_examen.dto.response.EtudiantResponseDto;
import com.example.application_web_examen.dto.response.ProfResponseDto;
import com.example.application_web_examen.model.*;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    // Admin Mappings
    AdminResponseDto toAdminResponseDto(Admin admin);
    Admin toAdminEntity(AdminRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Admin partialUpdateAdmin(AdminRequestDto adminRequestDto, @MappingTarget Admin admin);

    // Artisan Mappings
    ProfResponseDto toProfResponseDto(Prof prof);
    Prof toProfEntity(ProfRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prof partialUpdateProf(ProfRequestDto profRequestDto, @MappingTarget Prof prof);

    // Customer Mappings
    EtudiantResponseDto toEtudiantResponseDto(Etudiant etudiant);
    Etudiant toEtudiantEntity(EtudiantRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Etudiant partialUpdateEtudiant(EtudiantRequestDto etudiantRequestDto, @MappingTarget Etudiant etudiant);
}
