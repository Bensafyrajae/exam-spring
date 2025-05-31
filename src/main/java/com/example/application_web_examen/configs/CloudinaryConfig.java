package com.example.application_web_examen.configs;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpptinkpa");
        config.put("api_key", "553876483541722");
        config.put("api_secret", "iO49dvvVufGcB2H9awhp6Euybcc");
        return new Cloudinary(config);
    }
}
