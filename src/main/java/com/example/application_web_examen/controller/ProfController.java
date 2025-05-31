package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.ProfRequestDto;
import com.example.application_web_examen.dto.response.ProfResponseDto;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Prof;
import com.example.application_web_examen.service.ProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing profs.
 */
@RestController
@RequestMapping("/api/profs")
public class ProfController {

    private final ProfService profService;
    private final UserMapper userMapper;

    @Autowired
    public ProfController(ProfService profService, UserMapper userMapper) {
        this.profService = profService;
        this.userMapper = userMapper;
    }

    /**
     * Retrieve all profs.
     *
     * @return List of all profs.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ProfResponseDto>> getAllProfs() {
        List<Prof> Profs = profService.getAllProfs();
        List<ProfResponseDto> profResponseDtos = Profs.stream()
                .map(userMapper::toProfResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(profResponseDtos, HttpStatus.OK);
    }

    /**
     * Retrieve an prof by ID.
     *
     * @return The prof with the specified ID.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROF')")
    @GetMapping("/details")
    public ResponseEntity<ProfResponseDto> getArtisanById(@AuthenticationPrincipal Prof currentProf) {
        Prof prof = profService.getProfById(currentProf.getId());
        return prof != null
                ? new ResponseEntity<>(userMapper.toProfResponseDto(prof), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Update an existing prof.
     *
     * @param artisanDTO The prof with updated information.
     * @param userPhoto  The photo to be updated, if any.
     * @param prof    The authenticated prof.
     * @return The updated prof.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROF')")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfResponseDto> updateArtisan(
            @RequestPart("prof") ProfRequestDto artisanDTO,
            @RequestPart(value = "userPhoto", required = false) MultipartFile userPhoto,
            @AuthenticationPrincipal Prof prof) {

        Prof updatedProf = profService.updateProf(artisanDTO, prof, userPhoto);
        return new ResponseEntity<>(userMapper.toProfResponseDto(updatedProf), HttpStatus.OK);
    }
}
