package com.solidwall.tartib.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.solidwall.tartib.core.exceptions.FileStorageException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.studyforproject.CreateDto;
import com.solidwall.tartib.dto.studyforproject.UpdateDto;
import com.solidwall.tartib.entities.StudyForProject;
import com.solidwall.tartib.implementations.StudyForProjectImplementation;
import com.solidwall.tartib.repositories.StudyForProjectRepository;

import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StudyForProjectService implements StudyForProjectImplementation {

    @Value("${app.upload.project-study-docs}")
    private String uploadPath;

    @Autowired
    private StudyForProjectRepository studyForProjectRepository;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Path.of(uploadPath));
            log.info("Upload directory created/verified: {}", uploadPath);
        } catch (IOException e) {
            log.error("Could not create upload directory: {}", uploadPath, e);
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public List<StudyForProject> findAll() {
        return studyForProjectRepository.findAll();
    }

    @Override
    public StudyForProject getOne(Long id) {
        return studyForProjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("StudyForProject not found"));
    }

    @Override
    public StudyForProject findOne(Map<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public StudyForProject create(CreateDto data) {
        StudyForProject studyForProject = new StudyForProject();
        studyForProject.setName(data.getName());
        studyForProject.setState(data.getState());
        studyForProject.setDescription(data.getDescription());
        studyForProject.setRealisationDate(data.getRealisationDate());

        if (data.getStudyFile() != null) {
            String filePath = handleFileUpload(data.getStudyFile(), studyForProject.getName());
            studyForProject.setStudyFile(filePath);
        }

        return studyForProjectRepository.save(studyForProject);
    }

    @Override
    public StudyForProject update(Long id, UpdateDto data) {
        StudyForProject studyForProject = getOne(id);
        studyForProject.setName(data.getName());
        studyForProject.setState(data.getState());
        studyForProject.setDescription(data.getDescription());
        studyForProject.setRealisationDate(data.getRealisationDate());

        if (data.getStudyFile() != null) {
            deleteFileIfExists(studyForProject.getStudyFile());
            String filePath = handleFileUpload(data.getStudyFile(), studyForProject.getName());
            studyForProject.setStudyFile(filePath);
        }

        return studyForProjectRepository.save(studyForProject);
    }

    @Override
    public void delete(Long id) {
        StudyForProject studyForProject = getOne(id);
        deleteFileIfExists(studyForProject.getStudyFile());
        studyForProjectRepository.delete(studyForProject);
    }

    private String handleFileUpload(MultipartFile file, String name) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";
            String fileName = String.format("study_%s_%d.%s", name.replace(" ", "_"),
                    System.currentTimeMillis(), extension);
            Path targetLocation = Path.of(uploadPath, fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file", ex);
        }
    }

    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Files.deleteIfExists(Path.of(uploadPath, filePath));
            } catch (IOException ex) {
                log.error("Error deleting file: {}", filePath, ex);
            }
        }
    }

    public Resource downloadStudyFile(Long id) {
        try {
            StudyForProject studyForProject = getOne(id);

            if (studyForProject.getStudyFile() == null || studyForProject.getStudyFile().isEmpty()) {
                throw new NotFoundException("No study file found for this project");
            }

            Path filePath = Path.of(uploadPath, studyForProject.getStudyFile());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("File not found or not readable");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException("Error: " + e.getMessage());
        }
    }

    public String determineContentType(String filename) {
        if (filename.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        } else if (filename.toLowerCase().endsWith(".doc")) {
            return "application/msword";
        } else if (filename.toLowerCase().endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else {
            return "application/octet-stream";
        }
    }
}
