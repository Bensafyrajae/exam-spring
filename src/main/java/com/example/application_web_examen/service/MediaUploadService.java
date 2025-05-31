package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.CloudinaryResponse;
import com.example.application_web_examen.exception.FileUploadException;
import com.example.application_web_examen.exception.UnsupportedEntityException;
import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaUploadService {
    private final CloudinaryService cloudinaryService;

    @Autowired
    public MediaUploadService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public Media handleMediaUpload(MultipartFile file, Object entity) {
        try {
            FileUploadUtil.assertAllowed(file, FileUploadUtil.MEDIA_PATTERN);
            String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName, "auto");

            Media media = new Media();
            media.setMediaUrl(response.getUrl());
            media.setMediaId(response.getPublicId());

            if (entity instanceof User) {
                media.setUser((User) entity);
            } else {
                throw new UnsupportedEntityException("Unsupported entity type");
            }

            return media;
        } catch (Exception e) {
            throw new FileUploadException("Failed to handle media upload", e);
        }
    }
}
