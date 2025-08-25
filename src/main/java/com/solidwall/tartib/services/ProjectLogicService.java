package com.solidwall.tartib.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FileStorageException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.logic.CreateDto;
import com.solidwall.tartib.dto.project.logic.UpdateDto;
import com.solidwall.tartib.entities.ComponentLogicEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.implementations.ProjectLogicImplementation;
import com.solidwall.tartib.repositories.ComponentLogicRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;

import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProjectLogicService implements ProjectLogicImplementation {

    @Value("${app.upload.project-logic-docs}")
    private String uploadPath;

    @Autowired
    private ProjectLogicRepository projectLogicRepository;

    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;

    @Autowired
    private ComponentLogicRepository componentLogicRepository;

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
    public ProjectLogicEntity getOne(Long id) {
        return projectLogicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("project logic not exist"));
    }

    @Override
    public ProjectLogicEntity findOne(Map<String, String> data) {
        if (data.get("projectIdentity") != null) {
            Long projectId = Long.parseLong(data.get("projectIdentity"));
            ProjectIdentityEntity projectIdentity = projectIdentityRepository.findById(projectId)
                    .orElseThrow(() -> new NotFoundException("projectIdentity not found"));
            return projectLogicRepository.findByProjectIdentity(projectIdentity)
                    .orElseThrow(() -> new NotFoundException("projectIdentity logic not found"));
        }
        throw new BadRequestException("param not exist");
    }

    @Override
    public List<ProjectLogicEntity> findAll() {
        return projectLogicRepository.findAll();
    }

    @Override
    public ProjectLogicEntity create(CreateDto data) {
        ProjectIdentityEntity projectIdentity = projectIdentityRepository.findById(data.getProjectIdentity())
                .orElseThrow(() -> new NotFoundException("not exist any project identy to link this logic "));

        projectLogicRepository.findByProjectIdentity(projectIdentity).ifPresent(projectLogicRepository::delete);

        ProjectLogicEntity newProjectLogic = new ProjectLogicEntity();
        newProjectLogic.setProjectIdentity(projectIdentity);
        newProjectLogic.setGeneralObjective(data.getGeneralObjective());
        newProjectLogic.setSpecific_objective(data.getSpecific_objective());
        newProjectLogic.setResults(data.getResults());
        newProjectLogic.setYearEnd(data.getYearEnd());
        newProjectLogic.setYearStart(data.getYearStart());

        if (data.getDocumentCadreFile() != null) {
            String filePath = handleFileUpload(data.getDocumentCadreFile(), "cadre", projectIdentity.getId());
            newProjectLogic.setDocumentCadre(filePath);
        }

        if (data.getDocumentPlanTravailFile() != null) {
            String filePath = handleFileUpload(data.getDocumentPlanTravailFile(), "plan_travail", projectIdentity.getId());
            newProjectLogic.setDocumentPlanTravail(filePath);
        }

        if (data.getComponentLogics() != null) {
            List<ComponentLogicEntity> componentLogics = data.getComponentLogics().stream()
                    .map(componentLogicDto -> {
                        ComponentLogicEntity componentLogic = new ComponentLogicEntity();
                        componentLogic.setName(componentLogicDto.getName());
                        componentLogic.setDescription(componentLogicDto.getDescription());
                        componentLogic.setCout(componentLogicDto.getCout());
                        componentLogic.setProjectLogic(newProjectLogic);
                        return componentLogic;
                    })
                    .collect(Collectors.toList());
            newProjectLogic.setComponentLogics(componentLogics);
        }

        return projectLogicRepository.save(newProjectLogic);
    }

    @Override
    public ProjectLogicEntity update(Long id, UpdateDto data) {
        ProjectLogicEntity updateProjectLogic = getOne(id);
        updateProjectLogic.setGeneralObjective(data.getGeneralObjective());
        updateProjectLogic.setSpecific_objective(data.getSpecific_objective());
        updateProjectLogic.setResults(data.getResults());
        updateProjectLogic.setYearEnd(data.getYearEnd());
        updateProjectLogic.setYearStart(data.getYearStart());

        if (data.getDocumentCadreFile() != null) {
            deleteFileIfExists(updateProjectLogic.getDocumentCadre());
            String filePath = handleFileUpload(data.getDocumentCadreFile(), "cadre", updateProjectLogic.getProjectIdentity().getId());
            updateProjectLogic.setDocumentCadre(filePath);
        }

        if (data.getDocumentPlanTravailFile() != null) {
            deleteFileIfExists(updateProjectLogic.getDocumentPlanTravail());
            String filePath = handleFileUpload(data.getDocumentPlanTravailFile(), "plan_travail", updateProjectLogic.getProjectIdentity().getId());
            updateProjectLogic.setDocumentPlanTravail(filePath);
        }

        if (data.getComponentLogics() != null) {
            componentLogicRepository.deleteByProjectLogicId(updateProjectLogic.getId());
            List<ComponentLogicEntity> componentLogics = data.getComponentLogics().stream()
                    .map(componentLogicDto -> {
                        ComponentLogicEntity componentLogic = new ComponentLogicEntity();
                        componentLogic.setProjectLogic(updateProjectLogic);
                        componentLogic.setName(componentLogicDto.getName());
                        componentLogic.setDescription(componentLogicDto.getDescription());
                        componentLogic.setCout(componentLogicDto.getCout());
                        return componentLogic;
                    })
                    .collect(Collectors.toList());
            updateProjectLogic.setComponentLogics(componentLogics);
        }

        return projectLogicRepository.save(updateProjectLogic);
    }

    @Override
    public void delete(Long id) {
        ProjectLogicEntity projectLogic = getOne(id);
        deleteFileIfExists(projectLogic.getDocumentCadre());
        deleteFileIfExists(projectLogic.getDocumentPlanTravail());
        projectLogicRepository.delete(projectLogic);
    }

    @Override
    public Long getSommeComposant(Long id) {
        ProjectLogicEntity projectLogic = getOne(id);
        long somme = 0;
        if (projectLogic.getComponentLogics() != null) {
            somme = projectLogic.getComponentLogics().stream()
                    .mapToLong(ComponentLogicEntity::getCout)
                    .sum();
        }
        return somme;
    }

    private String handleFileUpload(MultipartFile file, String fileType, Long projectId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";
            String fileName = String.format("project_%d_%s_%d.%s", projectId, fileType,
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

    public Resource downloadDocumentCadreFile(Long id) {
        try {
            ProjectLogicEntity projectLogic = getOne(id);

            if (projectLogic.getDocumentCadre() == null || projectLogic.getDocumentCadre().isEmpty()) {
                throw new NotFoundException("No document cadre file found for this project logic");
            }

            Path filePath = Path.of(uploadPath, projectLogic.getDocumentCadre());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("File not found or not readable");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException("Error: " + e.getMessage());
        }
    }

    public Resource downloadDocumentPlanTravailFile(Long id) {
        try {
            ProjectLogicEntity projectLogic = getOne(id);

            if (projectLogic.getDocumentPlanTravail() == null || projectLogic.getDocumentPlanTravail().isEmpty()) {
                throw new NotFoundException("No document plan travail file found for this project logic");
            }

            Path filePath = Path.of(uploadPath, projectLogic.getDocumentPlanTravail());
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
