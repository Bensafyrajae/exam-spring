package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.ExamenRequestDto;
import com.example.application_web_examen.dto.response.ExamenResponseDto;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examens")
public class ExamenController {

    private final ExamenService examenService;

    @Autowired
    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @PostMapping
    public ResponseEntity<ExamenResponseDto> createExamen(
            @RequestBody ExamenRequestDto examenRequestDto,
            @AuthenticationPrincipal User user) {
        ExamenResponseDto createdExamen = examenService.createExamen(examenRequestDto, user.getId());
        return new ResponseEntity<>(createdExamen, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @GetMapping
    public ResponseEntity<List<ExamenResponseDto>> getExamensByProf(@AuthenticationPrincipal User user) {
        List<ExamenResponseDto> examens = examenService.getExamensByProf(user.getId());
        return ResponseEntity.ok(examens);
    }

    @PreAuthorize("hasAnyRole('ROLE_PROF', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ExamenResponseDto> getExamenById(@PathVariable Long id) {
        ExamenResponseDto examen = examenService.getExamenById(id);
        return ResponseEntity.ok(examen);
    }

    @GetMapping("/lien/{lienUnique}")
    public ResponseEntity<ExamenResponseDto> getExamenByLienUnique(@PathVariable String lienUnique) {
        ExamenResponseDto examen = examenService.getExamenByLienUnique(lienUnique);
        return ResponseEntity.ok(examen);
    }

    @PreAuthorize("hasRole('ROLE_PROF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamen(@PathVariable Long id, @AuthenticationPrincipal User user) {
        // Vérification supplémentaire possible pour s'assurer que l'examen appartient au prof
        examenService.deleteExamen(id);
        return ResponseEntity.noContent().build();
    }
}