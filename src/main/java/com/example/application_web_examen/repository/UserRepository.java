package com.example.application_web_examen.repository;

import com.example.application_web_examen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


    Boolean existsByEmail(String username);
}
