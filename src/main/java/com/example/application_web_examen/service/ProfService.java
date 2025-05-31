package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.ProfRequestDto;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Prof;
import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.repository.ProfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProfService {

    private final ProfRepository profRepository;
    private final MediaService mediaService;
    private final UserMapper userMapper;

    @Autowired
    public ProfService(ProfRepository profRepository, MediaService mediaService, UserMapper userMapper) {
        this.profRepository = profRepository;
        this.mediaService = mediaService;
        this.userMapper = userMapper;
    }

    public List<Prof> getAllProfs() {
        return profRepository.findAll();
    }

    public Prof getProfById(Long id) {
        return profRepository.findById(id).orElse(null);
    }

    public void deleteProf(Long id) {
        profRepository.deleteById(id);
    }

    public Prof updateProf(ProfRequestDto profDTO, Prof prof, MultipartFile userPhoto) {
        Prof newProf = userMapper.partialUpdateProf(profDTO, prof);

        if (userPhoto != null && !userPhoto.isEmpty()) {
            Media media=  mediaService.updateMediaForUser(userPhoto, newProf);
            newProf.setUserPhoto(media);
            return profRepository.save(newProf);
        }

        return profRepository.save(newProf);
    }
}
