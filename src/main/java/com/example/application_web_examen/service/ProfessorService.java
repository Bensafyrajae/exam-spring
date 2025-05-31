package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.response.ProfessorResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Professor;
import com.example.application_web_examen.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UserMapper userMapper;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository, UserMapper userMapper) {
        this.professorRepository = professorRepository;
        this.userMapper = userMapper;
    }

    public List<ProfessorResponseDto> getAllProfessors() {
        List<Professor> professors = professorRepository.findAll();
        return professors.stream()
                .map(userMapper::toProfessorResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ProfessorResponseDto> getAllProfessorsPaginated(Pageable pageable) {
        Page<Professor> professors = professorRepository.findAll(pageable);
        return professors.map(userMapper::toProfessorResponseDto);
    }

    public ProfessorResponseDto getProfessorById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        return userMapper.toProfessorResponseDto(professor);
    }

    public ProfessorResponseDto getProfessorByUsername(String username) {
        Professor professor = professorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        return userMapper.toProfessorResponseDto(professor);
    }

    public ProfessorResponseDto getProfessorByEmail(String email) {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        return userMapper.toProfessorResponseDto(professor);
    }

    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        professorRepository.delete(professor);
    }

    public boolean existsByUsername(String username) {
        return professorRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return professorRepository.existsByEmail(email);
    }

    public long getTotalProfessorsCount() {
        return professorRepository.count();
    }

    public List<ProfessorResponseDto> searchProfessors(String searchTerm) {
        return getAllProfessors().stream()
                .filter(professor ->
                        professor.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                professor.getUsername().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                professor.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .collect(Collectors.toList());
    }
}