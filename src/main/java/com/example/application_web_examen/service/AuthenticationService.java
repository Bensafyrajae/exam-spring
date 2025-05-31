package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.LoginRequestDto;
import com.example.application_web_examen.dto.request.ProfessorRequestDto;
import com.example.application_web_examen.dto.request.StudentRequestDto;
import com.example.application_web_examen.dto.response.LoginResponseDto;
import com.example.application_web_examen.exception.UserAlreadyExistsException;
import com.example.application_web_examen.exception.UserNotFoundException;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.model.Student;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.repository.ProfessorRepository;
import com.example.application_web_examen.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(StudentRepository studentRepository,
                                 ProfessorRepository professorRepository,
                                 UserMapper userMapper,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public Student registerStudent(StudentRequestDto studentDto) {
        if (studentRepository.existsByUsername(studentDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Student student = userMapper.toStudentEntity(studentDto);
        student.setPassword(passwordEncoder.encode(studentDto.getPassword()));
        return studentRepository.save(student);
    }

    @Transactional
    public Professor registerProfessor(ProfessorRequestDto professorDto) {
        if (professorRepository.existsByUsername(professorDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (professorRepository.existsByEmail(professorDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Professor professor = userMapper.toProfessorEntity(professorDto);
        professor.setPassword(passwordEncoder.encode(professorDto.getPassword()));
        return professorRepository.save(professor);
    }

    public LoginResponseDto authenticate(LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return new LoginResponseDto(
                    token,
                    jwtService.getExpirationTime(),
                    user.getRole().name(),
                    user.getId(),
                    user.getFullName()
            );
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid credentials");
        }
    }

    public void logout(String token) {
        // Vous pouvez implémenter une blacklist de tokens ici si nécessaire
        // Pour l'instant, le logout se fait côté client en supprimant le token
    }

    public boolean validateToken(String token) {
        try {
            return jwtService.isTokenValid(token, null);
        } catch (Exception e) {
            return false;
        }
    }
}