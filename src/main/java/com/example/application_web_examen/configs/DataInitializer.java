package com.example.application_web_examen.configs;

import com.example.application_web_examen.model.Admin;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setUsername("admin");
            admin.setFullName("Super Admin");
            userRepository.save(admin);
        }
    }
}

