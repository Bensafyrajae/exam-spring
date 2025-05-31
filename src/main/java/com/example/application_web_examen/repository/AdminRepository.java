package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Add any custom queries if necessary
}
