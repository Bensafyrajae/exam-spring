package com.example.application_web_examen.service;

import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public Media saveImage(MultipartFile file) throws IOException {
        validateFile(file);

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Media media = new Media();
        media.setNom(fileName);
        media.setType(file.getContentType());
        media.setContenu(file.getBytes());
        media.setUrl(baseUrl + "/api/media/" + media.getId());

        return mediaRepository.save(media);
    }

    @Transactional
    public String saveQuestionImage(MultipartFile file) throws IOException {
        validateFile(file);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return baseUrl + "/api/media/images/" + fileName;
    }

    @Transactional
    public Media saveUserPhoto(MultipartFile file) throws IOException {
        validateFile(file);

        String fileName = "user_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Media media = new Media();
        media.setNom(fileName);
        media.setType(file.getContentType());
        media.setContenu(file.getBytes());
        media.setUrl(baseUrl + "/api/media/" + media.getId());

        return mediaRepository.save(media);
    }

    public Media getMediaById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    public byte[] getImageContent(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id).orElse(null);
        if (media != null) {
            try {
                // Delete file from filesystem if it exists
                String fileName = media.getNom();
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + e.getMessage());
            }

            mediaRepository.deleteById(id);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Check file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds maximum limit of 10MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp")
        );
    }
}