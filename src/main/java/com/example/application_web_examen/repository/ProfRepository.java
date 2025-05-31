package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Prof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfRepository extends JpaRepository<Prof, Long> {}
