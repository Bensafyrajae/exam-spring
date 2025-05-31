package com.example.application_web_examen.service;

import com.example.application_web_examen.dto.request.EtudiantRequestDto;
import com.example.application_web_examen.mapper.UserMapper;
import com.example.application_web_examen.model.Etudiant;
import com.example.application_web_examen.model.Media;
import com.example.application_web_examen.repository.EtudiantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserMapper userMapper;

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }

    @Transactional
    public Etudiant updateEtudiant(EtudiantRequestDto etudiantDTO, Etudiant etudiant, MultipartFile userPhoto) {
        Etudiant updatedEtudiant = userMapper.partialUpdateEtudiant(etudiantDTO, etudiant);

        if (userPhoto != null && !userPhoto.isEmpty()) {
            Media media=  mediaService.updateMediaForUser(userPhoto, updatedEtudiant);
            updatedEtudiant.setUserPhoto(media);
            return etudiantRepository.save(updatedEtudiant);
        }

        return etudiantRepository.save(updatedEtudiant);
    }
}
