package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.LoginUserDto;
import com.example.application_web_examen.dto.request.AdminRequestDto;
import com.example.application_web_examen.dto.request.ProfRequestDto;
import com.example.application_web_examen.dto.request.EtudiantRequestDto;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Admin;
import com.example.application_web_examen.model.Prof;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(EtudiantRequestDto input) {
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEtudiantEntity(input);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public Prof addProf(ProfRequestDto input) {
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Prof prof = userMapper.toProfEntity(input);
        prof.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(prof);
    }

    public Admin addAdmin(AdminRequestDto input) {
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Admin admin = userMapper.toAdminEntity(input);
        admin.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(admin);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUserNameOrEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsernameOrEmail(input.getUserNameOrEmail(), input.getUserNameOrEmail());
    }
}
