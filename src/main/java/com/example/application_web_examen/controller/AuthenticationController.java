package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.*;
import com.example.application_web_examen.dto.request.AdminRequestDto;
import com.example.application_web_examen.dto.request.ProfRequestDto;
import com.example.application_web_examen.dto.request.EtudiantRequestDto;
import com.example.application_web_examen.dto.response.AdminResponseDto;
import com.example.application_web_examen.dto.response.EtudiantResponseDto;
import com.example.application_web_examen.dto.response.ProfResponseDto;
import com.example.application_web_examen.enums.Role;
import com.example.application_web_examen.exception.UserNotFoundException;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Admin;
import com.example.application_web_examen.model.Prof;
import com.example.application_web_examen.model.Etudiant;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.service.AuthenticationService;
import com.example.application_web_examen.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-etudiant")
    public ResponseEntity<EtudiantResponseDto> register(@RequestBody EtudiantRequestDto etudiantDTO) {
        User newEtudiant = authenticationService.signup(etudiantDTO);
        EtudiantResponseDto responseDto = userMapper.toEtudiantResponseDto((Etudiant) newEtudiant);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-prof")
    public ResponseEntity<ProfResponseDto> addArtisan(@RequestBody ProfRequestDto profDTO) {
        User newProf = authenticationService.addProf(profDTO);
        ProfResponseDto responseDto = userMapper.toProfResponseDto((Prof) newProf);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-admin")
    public ResponseEntity<AdminResponseDto> addAdmin(@RequestBody AdminRequestDto adminDTO) {
        User newAdmin = authenticationService.addAdmin(adminDTO);
        AdminResponseDto responseDto = userMapper.toAdminResponseDto((Admin) newAdmin); // Assuming newAdmin is of type Admin
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            Role role = authenticatedUser.getRole();

            String jwtToken = jwtService.generateToken(authenticatedUser, role);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }
}
