package com.example.application_web_examen.service;

import com.example.application_web_examen.repository.ProfessorRepository;
import com.example.application_web_examen.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public CustomUserDetailsService(StudentRepository studentRepository, ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find student first
        return studentRepository.findByUsername(username)
                .map(student -> (UserDetails) student)
                .orElseGet(() -> professorRepository.findByUsername(username)
                        .map(professor -> (UserDetails) professor)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)));
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Try to find student first
        return studentRepository.findByEmail(email)
                .map(student -> (UserDetails) student)
                .orElseGet(() -> professorRepository.findByEmail(email)
                        .map(professor -> (UserDetails) professor)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }
}