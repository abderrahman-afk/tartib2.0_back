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
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.plan.CreateDto;
import com.solidwall.tartib.dto.project.plan.UpdateDto;
import com.solidwall.tartib.entities.FinancialSourceEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import com.solidwall.tartib.entities.RubriqueEntity;
import com.solidwall.tartib.implementations.ProjectPlanImplementation;
import com.solidwall.tartib.repositories.FinancialSourceRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;
import com.solidwall.tartib.repositories.ProjectScaleRepository;
import com.solidwall.tartib.repositories.RubriqueRepository;

import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProjectPlanService implements ProjectPlanImplementation {

    @Value("${app.upload.project-plan-docs}")
    private String uploadPath;

    @Autowired
    private ProjectPlanRepository projectPlanRepository;

    @Autowired
    private ProjectIdentityRepository projectIdentityRepository;

    @Autowired
    private FinancialSourceRepository financialSourceRepository;

    @Autowired
    private RubriqueRepository rubriqueRepository;

    @Autowired
    private ProjectScaleRepository projectScaleRepository;

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
    public List<ProjectPlanEntity> findAll() {
        return projectPlanRepository.findAll();
    }

    @Override
    public ProjectPlanEntity getOne(Long id) {
        return projectPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("project plan not exist"));
    }

    @Override
    public ProjectPlanEntity findOne(Map<String, String> data) {
        if (data.get("projectIdentity") != null) {
            Long projectId = Long.parseLong(data.get("projectIdentity"));
            ProjectIdentityEntity projectIdentity = projectIdentityRepository.findById(projectId)
                    .orElse(null);
            if (projectIdentity == null) {
                return null;
            }
            return projectPlanRepository.findByProjectIdentity(projectIdentity).orElse(null);
        }
        throw new BadRequestException("param not exist");
    }

    @Override
    public ProjectPlanEntity create(CreateDto data) {
        ProjectIdentityEntity project = projectIdentityRepository.findById(data.getProjectIdentity())
                .orElseThrow(() -> new NotFoundException("project not found"));

        ProjectPlanEntity newProjectPlan = new ProjectPlanEntity();
        newProjectPlan.setProjectIdentity(project);
        newProjectPlan.setCoutTotale(data.getCoutTotale());
        newProjectPlan.setCoutDinars(data.getCoutDinars());
        newProjectPlan.setMontantAnnuel(data.getMontantAnnuel());
        newProjectPlan.setObservation(data.getObservation());
        newProjectPlan.setTauxEchange(data.getTauxEchange());

        if (data.getPlanFinancementFile() != null) {
            String filePath = handleFileUpload(data.getPlanFinancementFile(), project.getId());
            newProjectPlan.setPlanFinancement(filePath);
        }

        if (data.getFinancialSource() != null) {
            List<FinancialSourceEntity> financialSources = data.getFinancialSource().stream()
                    .map(financialSourceDto -> {
                        FinancialSourceEntity financialSource = new FinancialSourceEntity();
                        financialSource.setBailleur(financialSourceDto.getBailleur());
                        financialSource.setStatut(financialSourceDto.getStatut());
                        financialSource.setTauxEchange(financialSourceDto.getTauxEchange());
                        financialSource.setDevise(financialSourceDto.getDevise());
                        financialSource.setType(financialSourceDto.getType());
                        financialSource.setMontant(financialSourceDto.getMontant());
                        financialSource.setMontantDinars(financialSourceDto.getMontantDinars());
                        financialSource.setProjectPlan(newProjectPlan);
                        return financialSource;
                    })
                    .collect(Collectors.toList());
            newProjectPlan.setFinancialSource(financialSources);
        }

        if (data.getRubriques() != null) {
            List<RubriqueEntity> rubriques = data.getRubriques().stream()
                    .map(rubriqueDTO -> {
                        RubriqueEntity rub = new RubriqueEntity();
                        rub.setAmount(rubriqueDTO.getAmount());
                        rub.setName(rubriqueDTO.getName());
                        rub.setProjectPlan(newProjectPlan);
                        return rub;
                    })
                    .collect(Collectors.toList());
            newProjectPlan.setRubriques(rubriques);
        }

        ProjectScaleEntity scale = projectScaleRepository
                .findByMinimumBudgetLessThanEqualAndMaximumBudgetGreaterThanEqual(data.getCoutTotale(),
                        data.getCoutTotale());
        if (scale != null) {
            List<ProjectScaleEntity> projectScaleEntitycheck = projectScaleRepository
                    .findByMaximumBudgetAndMinimumBudget(scale.getMinimumBudget(), scale.getMaximumBudget());
            if (!projectScaleEntitycheck.isEmpty() && !projectScaleEntitycheck.contains(scale)) {
                throw new FoundException("cet intervale intersect avec un autre intervalle du budget");
            }
            scale.setProjectIdentity(project);
            projectScaleRepository.save(scale);
        }

        projectPlanRepository.findByProjectIdentity(project).ifPresent(projectPlanRepository::delete);

        return projectPlanRepository.save(newProjectPlan);
    }

    @Override
    @Transactional
    public ProjectPlanEntity update(Long id, UpdateDto data) {
        ProjectPlanEntity updateProjectPlan = getOne(id);
        ProjectIdentityEntity project = projectIdentityRepository.findById(data.getProjectIdentity())
                .orElseThrow(() -> new NotFoundException("project not found"));

        updateProjectPlan.setProjectIdentity(project);
        updateProjectPlan.setCoutTotale(data.getCoutTotale());
        updateProjectPlan.setCoutDinars(data.getCoutDinars());
        updateProjectPlan.setMontantAnnuel(data.getMontantAnnuel());
        updateProjectPlan.setObservation(data.getObservation());
        updateProjectPlan.setTauxEchange(data.getTauxEchange());

        if (data.getPlanFinancementFile() != null) {
            deleteFileIfExists(updateProjectPlan.getPlanFinancement());
            String filePath = handleFileUpload(data.getPlanFinancementFile(), project.getId());
            updateProjectPlan.setPlanFinancement(filePath);
        }

        if (data.getFinancialSource() != null) {
            financialSourceRepository.deleteByProjectPlanId(updateProjectPlan.getId());
            List<FinancialSourceEntity> financialSources = data.getFinancialSource().stream()
                    .map(financialSourceDto -> {
                        FinancialSourceEntity financialSource = new FinancialSourceEntity();
                        financialSource.setStatut(financialSourceDto.getStatut());
                        financialSource.setTauxEchange(financialSourceDto.getTauxEchange());
                        financialSource.setBailleur(financialSourceDto.getBailleur());
                        financialSource.setType(financialSourceDto.getType());
                        financialSource.setDevise(financialSourceDto.getDevise());
                        financialSource.setMontant(financialSourceDto.getMontant());
                        financialSource.setMontantDinars(financialSourceDto.getMontantDinars());
                        financialSource.setProjectPlan(updateProjectPlan);
                        return financialSource;
                    })
                    .collect(Collectors.toList());
            updateProjectPlan.setFinancialSource(financialSources);
        }

        if (data.getRubriques() != null) {
            rubriqueRepository.deleteByProjectPlanId(updateProjectPlan.getId());
            List<RubriqueEntity> rubriques = data.getRubriques().stream()
                    .map(rubriqueDTO -> {
                        RubriqueEntity rub = new RubriqueEntity();
                        rub.setAmount(rubriqueDTO.getAmount());
                        rub.setName(rubriqueDTO.getName());
                        rub.setProjectPlan(updateProjectPlan);
                        return rub;
                    })
                    .collect(Collectors.toList());
            updateProjectPlan.setRubriques(rubriques);
        }

        return projectPlanRepository.save(updateProjectPlan);
    }

    @Override
    public void delete(Long id) {
        ProjectPlanEntity projectPlan = getOne(id);
        deleteFileIfExists(projectPlan.getPlanFinancement());
        projectPlanRepository.delete(projectPlan);
    }

    private String handleFileUpload(MultipartFile file, Long projectId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";
            String fileName = String.format("project_%d_plan_financement_%d.%s", projectId,
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

    public Resource downloadPlanFinancementFile(Long id) {
        try {
            ProjectPlanEntity projectPlan = getOne(id);

            if (projectPlan.getPlanFinancement() == null || projectPlan.getPlanFinancement().isEmpty()) {
                throw new NotFoundException("No plan financement file found for this project plan");
            }

            Path filePath = Path.of(uploadPath, projectPlan.getPlanFinancement());
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
