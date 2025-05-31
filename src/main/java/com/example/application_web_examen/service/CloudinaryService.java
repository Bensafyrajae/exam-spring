package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.CloudinaryResponse;
import com.example.application_web_examen.exception.FileUploadException;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName, String resourceType) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), Map.of(
                    "public_id", "nhndev/product/" + fileName,
                    "resource_type", resourceType
            ));

            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            if (url == null || publicId == null) {
                throw new FileUploadException("Cloudinary upload did not return expected results");
            }

            return CloudinaryResponse.builder().publicId(publicId).url(url).build();
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload file to Cloudinary", e);
        }
    }
}