package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.response.StudentResponseDto;
import com.example.application_web_examen.exception.ResourceNotFoundException;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Student;
import com.example.application_web_examen.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserMapper userMapper;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserMapper userMapper) {
        this.studentRepository = studentRepository;
        this.userMapper = userMapper;
    }

    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(userMapper::toStudentResponseDto)
                .collect(Collectors.toList());
    }

    public Page<StudentResponseDto> getAllStudentsPaginated(Pageable pageable) {
        Page<Student> students = studentRepository.findAll(pageable);
        return students.map(userMapper::toStudentResponseDto);
    }

    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return userMapper.toStudentResponseDto(student);
    }

    public StudentResponseDto getStudentByUsername(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return userMapper.toStudentResponseDto(student);
    }

    public StudentResponseDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return userMapper.toStudentResponseDto(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        studentRepository.delete(student);
    }

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public long getTotalStudentsCount() {
        return studentRepository.count();
    }

    public List<StudentResponseDto> searchStudents(String searchTerm) {
        // This would require custom repository methods
        return getAllStudents().stream()
                .filter(student ->
                        student.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                student.getUsername().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                student.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .collect(Collectors.toList());
    }
}