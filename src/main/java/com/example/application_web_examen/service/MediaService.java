package com.example.application_web_examen.service;

import com.example.application_web_examen.enums.Type;
import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.model.User;
import com.example.application_web_examen.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {
    private final MediaUploadService mediaUploadService;
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaUploadService mediaUploadService, MediaRepository mediaRepository) {
        this.mediaUploadService = mediaUploadService;
        this.mediaRepository = mediaRepository;
    }

    public Media updateMediaForUser(MultipartFile userPhoto, User user) {
        Media media;

        if (user.getUserPhoto() != null) {
            media = user.getUserPhoto();
            Media newMedia = mediaUploadService.handleMediaUpload(userPhoto, user);
            media.setMediaUrl(newMedia.getMediaUrl());
            media.setMediaId(newMedia.getMediaId());
            media.setType(Type.PHOTO);
            mediaRepository.save(media);
        } else {
            media = mediaUploadService.handleMediaUpload(userPhoto, user);
            media.setType(Type.PHOTO);
            mediaRepository.save(media);
            user.setUserPhoto(media);
        }

        return media;
    }
}
