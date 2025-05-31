package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.ReponseRequestDto;
import com.example.application_web_examen.dto.response.ReponseResponseDto;
import com.example.application_web_examen.dto.response.ScoreResponseDto;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.service.ReponseService;
import com.example.application_web_examen.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses")
public class ReponseController {

    private final ReponseService reponseService;
    private final ScoreService scoreService;

    @Autowired
    public ReponseController(ReponseService reponseService, ScoreService scoreService) {
        this.reponseService = reponseService;
        this.scoreService = scoreService;
    }

    @PostMapping
    public ResponseEntity<ReponseResponseDto> enregistrerReponse(
            @RequestBody ReponseRequestDto reponseRequestDto) {
        ReponseResponseDto savedReponse = reponseService.enregistrerReponse(reponseRequestDto);
        return new ResponseEntity<>(savedReponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_PROF', 'ROLE_ADMIN')")
    @GetMapping("/etudiant/{etudiantId}/examen/{examenId}")
    public ResponseEntity<List<ReponseResponseDto>> getReponsesByEtudiantAndExamen(
            @PathVariable Long etudiantId,
            @PathVariable Long examenId) {
        List<ReponseResponseDto> reponses = reponseService.getReponsesByEtudiantAndExamen(etudiantId, examenId);
        return ResponseEntity.ok(reponses);
    }

    @GetMapping("/score/etudiant/{etudiantId}/examen/{examenId}")
    public ResponseEntity<ScoreResponseDto> getScoreByEtudiantAndExamen(
            @PathVariable Long etudiantId,
            @PathVariable Long examenId) {
        ScoreResponseDto score = scoreService.calculateScore(etudiantId, examenId);
        return ResponseEntity.ok(score);
    }

    @PreAuthorize("hasRole('ROLE_ETUDIANT')")
    @GetMapping("/mon-score/examen/{examenId}")
    public ResponseEntity<ScoreResponseDto> getMyScore(
            @PathVariable Long examenId,
            @AuthenticationPrincipal User user) {
        ScoreResponseDto score = scoreService.calculateScore(user.getId(), examenId);
        return ResponseEntity.ok(score);
    }
}