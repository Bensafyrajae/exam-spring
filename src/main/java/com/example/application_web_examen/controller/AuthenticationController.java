package com.example.application_web_examen.controller;

import com.example.application_web_examen.dto.request.LoginRequestDto;
import com.example.application_web_examen.dto.request.ProfessorRequestDto;
import com.example.application_web_examen.dto.request.StudentRequestDto;
import com.example.application_web_examen.dto.response.LoginResponseDto;
import com.example.application_web_examen.dto.response.ProfessorResponseDto;
import com.example.application_web_examen.dto.response.StudentResponseDto;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.model.Student;
import com.example.application_web_examen.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESSOR')")
    @PostMapping("/register/student")
    public ResponseEntity<StudentResponseDto> registerStudent(@RequestBody StudentRequestDto studentDto) {
        Student student = authenticationService.registerStudent(studentDto);
        StudentResponseDto response = userMapper.toStudentResponseDto(student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/professor")
    public ResponseEntity<ProfessorResponseDto> registerProfessor(@RequestBody ProfessorRequestDto professorDto) {
        Professor professor = authenticationService.registerProfessor(professorDto);
        ProfessorResponseDto response = userMapper.toProfessorResponseDto(professor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}