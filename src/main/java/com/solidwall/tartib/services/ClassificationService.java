package com.solidwall.tartib.services;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solidwall.tartib.core.exceptions.FileStorageException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.classementPortfolio.ClassificationResponseDto;
import com.solidwall.tartib.dto.classementPortfolio.CreateClassificationDto;
import com.solidwall.tartib.dto.classementPortfolio.GeographicBonusDto;
import com.solidwall.tartib.dto.classementPortfolio.GeographicBonusResponseDto;
import com.solidwall.tartib.dto.classementPortfolio.SecteurBonusDto;
import com.solidwall.tartib.dto.classementPortfolio.SecteurBonusResponseDto;
import com.solidwall.tartib.dto.classementPortfolio.UpdateClassificationDto;
import com.solidwall.tartib.entities.ClassificationEntity;
import com.solidwall.tartib.entities.EvaluationGridEntity;
import com.solidwall.tartib.entities.GeographicBonusEntity;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;
import com.solidwall.tartib.entities.NomenclatureSecteurEntity;
import com.solidwall.tartib.entities.SecteurBonusEntity;
import com.solidwall.tartib.implementations.ClassificationImplementation;
import com.solidwall.tartib.repositories.ClassificationRepository;
import com.solidwall.tartib.repositories.EvaluationGridRepository;
import com.solidwall.tartib.repositories.GeographicBonusRepository;
import com.solidwall.tartib.repositories.GeographicCategoryRepository;
import com.solidwall.tartib.repositories.NomenclatureGeographicRepository;
import com.solidwall.tartib.repositories.NomenclatureSecteurRepository;
import com.solidwall.tartib.repositories.SecteurBonusRepository;
import com.solidwall.tartib.repositories.SecteurRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;

import com.solidwall.tartib.core.exceptions.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ClassificationService implements ClassificationImplementation {

    @Value("${app.upload.classification-docs}")
    private String uploadPath;

    @Autowired
    private ClassificationRepository classificationRepository;
    @Autowired
    private SecteurBonusRepository secteurBonusRepository;

    @Autowired
    private GeographicBonusRepository geographicBonusRepository;
    @Autowired
    private NomenclatureSecteurRepository nomenclatureSecteurRepository;

    @Autowired
    private NomenclatureGeographicRepository nomenclatureGeographicRepository;

    @Autowired
    private EvaluationGridRepository evaluationGridRepository;

    @Autowired
    private SecteurRepository secteurRepository;

    @Autowired
    private GeographicCategoryRepository geographicCategoryRepository;

      ClassificationEntity createBaseClassification(CreateClassificationDto data,
            NomenclatureSecteurEntity nomenclatureSecteur,
            NomenclatureGeographicEntity nomenclatureGeographic,
            EvaluationGridEntity evaluationGrid) {

        ClassificationEntity classification = new ClassificationEntity();
        classification.setCode(data.getCode());
        classification.setTitle(data.getTitle());
        classification.setDescription(data.getDescription());
        classification.setYear(data.getYear());
        classification.setActive(false);
        classification.setNomenclatureSecteur(nomenclatureSecteur);
        classification.setNomenclatureGeographic(nomenclatureGeographic);
        classification.setEvaluationGrid(evaluationGrid);

        if (data.getJustificationFile() != null) {
            String filePath = handleFileUpload(data.getJustificationFile(), data.getCode());
            classification.setJustificationPath(filePath);
        }

        classification.setSecteurBonuses(new HashSet<>());
        classification.setGeographicBonuses(new HashSet<>());

        return classificationRepository.save(classification);
    }

      void addSecteurBonuses(String secteurBonusesJson, ClassificationEntity classification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SecteurBonusDto> secteurBonusDtos = mapper.readValue(
                    secteurBonusesJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, SecteurBonusDto.class));

            log.info("Processing {} secteur bonuses", secteurBonusDtos.size());

            Set<SecteurBonusEntity> secteurBonuses = secteurBonusDtos.stream()
                    .map(dto -> createSecteurBonus(dto, classification))
                    .collect(Collectors.toSet());

            classification.getSecteurBonuses().addAll(secteurBonuses);
        } catch (Exception e) {
            log.error("Error processing secteur bonuses: {}", e.getMessage(), e);
            throw new BadRequestException("Invalid secteur bonuses data: " + e.getMessage());
        }
    }

    private SecteurBonusEntity createSecteurBonus(SecteurBonusDto dto, ClassificationEntity classification) {
        SecteurBonusEntity bonus = new SecteurBonusEntity();
        bonus.setClassification(classification);
        bonus.setSecteur(secteurRepository.findById(dto.getSecteurId())
                .orElseThrow(() -> new NotFoundException("Secteur not found: " + dto.getSecteurId())));
        bonus.setBonusPercentage(dto.getBonusPercentage());
        bonus.setComment(dto.getComment());
        return bonus;
    }

      void addGeographicBonuses(String geographicBonusesJson, ClassificationEntity classification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<GeographicBonusDto> geographicBonusDtos = mapper.readValue(
                    geographicBonusesJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, GeographicBonusDto.class));

            log.info("Processing {} geographic bonuses", geographicBonusDtos.size());

            Set<GeographicBonusEntity> geographicBonuses = geographicBonusDtos.stream()
                    .map(dto -> createGeographicBonus(dto, classification))
                    .collect(Collectors.toSet());

            classification.getGeographicBonuses().addAll(geographicBonuses);
        } catch (Exception e) {
            log.error("Error processing geographic bonuses: {}", e.getMessage(), e);
            throw new BadRequestException("Invalid geographic bonuses data: " + e.getMessage());
        }
    }

    private GeographicBonusEntity createGeographicBonus(GeographicBonusDto dto, ClassificationEntity classification) {
        GeographicBonusEntity bonus = new GeographicBonusEntity();
        bonus.setClassification(classification);
        bonus.setGeographicCategory(geographicCategoryRepository.findById(dto.getGeographicCategoryId())
                .orElseThrow(() -> new NotFoundException(
                        "Geographic category not found: " + dto.getGeographicCategoryId())));
        bonus.setBonusPercentage(dto.getBonusPercentage());
        bonus.setComment(dto.getComment());
        return bonus;
    }

    @Override
    @Transactional
    public ClassificationResponseDto create(CreateClassificationDto data) {
        log.info("************starting **** \n Starting creation of classification with code: {}", data.getCode());
        log.info("************starting **** \n Starting creation of bonusesget GeographicBonuses: {}",
                data.getGeographicBonuses());
        log.info("************starting **** \n Starting creation of bonuses  getSecteurBonuses{}",
                data.getSecteurBonuses());

        // Validate references exist
        NomenclatureSecteurEntity nomenclatureSecteur = nomenclatureSecteurRepository
                .findById(data.getNomenclatureSecteurId())
                .orElseThrow(() -> new NotFoundException("Nomenclature secteur not found"));

        NomenclatureGeographicEntity nomenclatureGeographic = nomenclatureGeographicRepository
                .findById(data.getNomenclatureGeographicId())
                .orElseThrow(() -> new NotFoundException("Nomenclature geographic not found"));

        EvaluationGridEntity evaluationGrid = evaluationGridRepository
                .findById(data.getEvaluationGridId())
                .orElseThrow(() -> new NotFoundException("Evaluation grid not found"));

        // Create base classification
        ClassificationEntity classification = new ClassificationEntity();
        classification.setCode(data.getCode());
        classification.setTitle(data.getTitle());
        classification.setDescription(data.getDescription());
        classification.setYear(data.getYear());
        classification.setActive(false);
        classification.setNomenclatureSecteur(nomenclatureSecteur);
        classification.setNomenclatureGeographic(nomenclatureGeographic);
        classification.setEvaluationGrid(evaluationGrid);

        // Handle file upload if present
        if (data.getJustificationFile() != null) {
            String filePath = handleFileUpload(data.getJustificationFile(), data.getCode());
            classification.setJustificationPath(filePath);
        }

        // Initialize collections
        classification.setSecteurBonuses(new HashSet<>());
        classification.setGeographicBonuses(new HashSet<>());

        // Save and flush base classification
        final ClassificationEntity savedClassification = classificationRepository.saveAndFlush(classification);
        log.info("Saved base classification with ID: {}", savedClassification.getId());

        // Handle secteur bonuses
        if (data.getSecteurBonuses() != null && !data.getSecteurBonuses().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<SecteurBonusDto> secteurBonusDtos = mapper.readValue(
                        data.getSecteurBonuses(),
                        mapper.getTypeFactory().constructCollectionType(List.class, SecteurBonusDto.class));

                log.info("Processing {} secteur bonuses", secteurBonusDtos.size());

                List<SecteurBonusEntity> secteurBonuses = secteurBonusDtos.stream()
                        .map(dto -> {
                            SecteurBonusEntity bonus = new SecteurBonusEntity();
                            bonus.setClassification(savedClassification);
                            bonus.setSecteur(secteurRepository.findById(dto.getSecteurId())
                                    .orElseThrow(
                                            () -> new NotFoundException("Secteur not found: " + dto.getSecteurId())));
                            bonus.setBonusPercentage(dto.getBonusPercentage());
                            bonus.setComment(dto.getComment());
                            return bonus;
                        })
                        .collect(Collectors.toList());

                secteurBonusRepository.saveAll(secteurBonuses);
                secteurBonusRepository.flush();
                log.info("Saved {} secteur bonuses", secteurBonuses.size());

                savedClassification.getSecteurBonuses().addAll(secteurBonuses);
            } catch (Exception e) {
                log.error("Error processing secteur bonuses: {}", e.getMessage(), e);
                throw new BadRequestException("Invalid secteur bonuses data: " + e.getMessage());
            }
        }

        // Handle geographic bonuses
        if (data.getGeographicBonuses() != null && !data.getGeographicBonuses().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<GeographicBonusDto> geographicBonusDtos = mapper.readValue(
                        data.getGeographicBonuses(),
                        mapper.getTypeFactory().constructCollectionType(List.class, GeographicBonusDto.class));

                log.info("Processing {} geographic bonuses", geographicBonusDtos.size());

                List<GeographicBonusEntity> geographicBonuses = geographicBonusDtos.stream()
                        .map(dto -> {
                            GeographicBonusEntity bonus = new GeographicBonusEntity();
                            bonus.setClassification(savedClassification);
                            bonus.setGeographicCategory(geographicCategoryRepository
                                    .findById(dto.getGeographicCategoryId())
                                    .orElseThrow(() -> new NotFoundException(
                                            "Geographic category not found: " + dto.getGeographicCategoryId())));
                            bonus.setBonusPercentage(dto.getBonusPercentage());
                            bonus.setComment(dto.getComment());
                            return bonus;
                        })
                        .collect(Collectors.toList());

                geographicBonusRepository.saveAll(geographicBonuses);
                geographicBonusRepository.flush();
                log.info("Saved {} geographic bonuses", geographicBonuses.size());

                savedClassification.getGeographicBonuses().addAll(geographicBonuses);
            } catch (Exception e) {
                log.error("Error processing geographic bonuses: {}", e.getMessage(), e);
                throw new BadRequestException("Invalid geographic bonuses data: " + e.getMessage());
            }
        }

        // Final save to ensure all relationships are updated
        ClassificationEntity savedClassificationTwi = classificationRepository.saveAndFlush(savedClassification);

        // Reload to ensure we have all data
        ClassificationEntity finalClassification = classificationRepository.findById(savedClassificationTwi.getId())
                .orElseThrow(() -> new NotFoundException("Classification not found after save"));

        log.info("Completed classification creation. ID: {}, Secteur bonuses: {}, Geographic bonuses: {}",
                finalClassification.getId(),
                finalClassification.getSecteurBonuses().size(),
                finalClassification.getGeographicBonuses().size());

        return mapToDto(finalClassification);
    }

    @Override
    @Transactional

    public ClassificationResponseDto update(Long id, UpdateClassificationDto data) {
        log.info("Starting update of classification ID: {}", id);
        log.info("Geographic bonuses data: {}", data.getGeographicBonuses());
        log.info("Secteur bonuses data: {}", data.getSecteurBonuses());

        ClassificationEntity classification = classificationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Classification not found"));

        // Update basic fields
        classification.setCode(data.getCode());
        classification.setTitle(data.getTitle());
        classification.setDescription(data.getDescription());

        // Update references if changed
        if (!classification.getNomenclatureSecteur().getId().equals(data.getNomenclatureSecteurId())) {
            classification.setNomenclatureSecteur(nomenclatureSecteurRepository
                    .findById(data.getNomenclatureSecteurId())
                    .orElseThrow(() -> new NotFoundException("Nomenclature secteur not found")));
        }

        if (!classification.getNomenclatureGeographic().getId().equals(data.getNomenclatureGeographicId())) {
            classification.setNomenclatureGeographic(nomenclatureGeographicRepository
                    .findById(data.getNomenclatureGeographicId())
                    .orElseThrow(() -> new NotFoundException("Nomenclature geographic not found")));
        }

        if (!classification.getEvaluationGrid().getId().equals(data.getEvaluationGridId())) {
            classification.setEvaluationGrid(evaluationGridRepository
                    .findById(data.getEvaluationGridId())
                    .orElseThrow(() -> new NotFoundException("Evaluation grid not found")));
        }

        // Handle file update
        if (data.getJustificationFile() != null) {
            deleteFileIfExists(classification.getJustificationPath());
            String filePath = handleFileUpload(data.getJustificationFile(), data.getCode());
            classification.setJustificationPath(filePath);
        }

        // Save initial changes and flush
        ClassificationEntity savedClassification = classificationRepository.saveAndFlush(classification);

        // Clear existing bonuses
        secteurBonusRepository.deleteAll(savedClassification.getSecteurBonuses());
        geographicBonusRepository.deleteAll(savedClassification.getGeographicBonuses());
        secteurBonusRepository.flush();
        geographicBonusRepository.flush();

        savedClassification.getSecteurBonuses().clear();
        savedClassification.getGeographicBonuses().clear();

        // Update secteur bonuses
        if (data.getSecteurBonuses() != null && !data.getSecteurBonuses().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<SecteurBonusDto> secteurBonusDtos = mapper.readValue(
                        data.getSecteurBonuses(),
                        mapper.getTypeFactory().constructCollectionType(List.class, SecteurBonusDto.class));

                log.info("Processing {} secteur bonuses for update", secteurBonusDtos.size());

                List<SecteurBonusEntity> secteurBonuses = secteurBonusDtos.stream()
                        .map(dto -> {
                            SecteurBonusEntity bonus = new SecteurBonusEntity();
                            bonus.setClassification(savedClassification);
                            bonus.setSecteur(secteurRepository.findById(dto.getSecteurId())
                                    .orElseThrow(
                                            () -> new NotFoundException("Secteur not found: " + dto.getSecteurId())));
                            bonus.setBonusPercentage(dto.getBonusPercentage());
                            bonus.setComment(dto.getComment());
                            return bonus;
                        })
                        .collect(Collectors.toList());

                secteurBonusRepository.saveAll(secteurBonuses);
                secteurBonusRepository.flush();
                log.info("Saved {} secteur bonuses", secteurBonuses.size());

                savedClassification.getSecteurBonuses().addAll(secteurBonuses);
            } catch (Exception e) {
                log.error("Error updating secteur bonuses: {}", e.getMessage(), e);
                throw new BadRequestException("Invalid secteur bonuses data: " + e.getMessage());
            }
        }

        // Update geographic bonuses
        if (data.getGeographicBonuses() != null && !data.getGeographicBonuses().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<GeographicBonusDto> geographicBonusDtos = mapper.readValue(
                        data.getGeographicBonuses(),
                        mapper.getTypeFactory().constructCollectionType(List.class, GeographicBonusDto.class));

                log.info("Processing {} geographic bonuses for update", geographicBonusDtos.size());

                List<GeographicBonusEntity> geographicBonuses = geographicBonusDtos.stream()
                        .map(dto -> {
                            GeographicBonusEntity bonus = new GeographicBonusEntity();
                            bonus.setClassification(savedClassification);
                            bonus.setGeographicCategory(geographicCategoryRepository
                                    .findById(dto.getGeographicCategoryId())
                                    .orElseThrow(() -> new NotFoundException(
                                            "Geographic category not found: " + dto.getGeographicCategoryId())));
                            bonus.setBonusPercentage(dto.getBonusPercentage());
                            bonus.setComment(dto.getComment());
                            return bonus;
                        })
                        .collect(Collectors.toList());

                geographicBonusRepository.saveAll(geographicBonuses);
                geographicBonusRepository.flush();
                log.info("Saved {} geographic bonuses", geographicBonuses.size());

                savedClassification.getGeographicBonuses().addAll(geographicBonuses);
            } catch (Exception e) {
                log.error("Error updating geographic bonuses: {}", e.getMessage(), e);
                throw new BadRequestException("Invalid geographic bonuses data: " + e.getMessage());
            }
        }

        // Final save to ensure all relationships are updated
        ClassificationEntity finalClassification = classificationRepository.saveAndFlush(savedClassification);

        // Reload to ensure we have all data
        ClassificationEntity reloadedClassification = classificationRepository.findById(finalClassification.getId())
                .orElseThrow(() -> new NotFoundException("Classification not found after update"));

        log.info("Completed classification update. ID: {}, Secteur bonuses: {}, Geographic bonuses: {}",
                reloadedClassification.getId(),
                reloadedClassification.getSecteurBonuses().size(),
                reloadedClassification.getGeographicBonuses().size());

        return mapToDto(reloadedClassification);
    }

    @Override
    public ClassificationResponseDto getOne(Long id) {
        return mapToDto(classificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Classification not found")));
    }

    @Override
    public List<ClassificationResponseDto> findAll(Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return classificationRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        return classificationRepository.findAll().stream()
                .filter(classification -> matchesFilters(classification, filters))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        // First deactivate all
        deactivateAll();

        // Then activate the selected one
        ClassificationEntity classification = classificationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Classification not found"));

        classification.setActive(true);
        classificationRepository.save(classification);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClassificationEntity classification = classificationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Classification not found"));

        deleteFileIfExists(classification.getJustificationPath());
        classificationRepository.delete(classification);
    }

    @Override
    public void deactivateAll() {
        classificationRepository.findAll().forEach(classification -> {
            classification.setActive(false);
            classificationRepository.save(classification);
        });
    }

    @Override
    public ClassificationResponseDto findActive() {
        return classificationRepository.findByActive(true)
                .map(this::mapToDto)
                .orElse(null);
    }

    // Helper methods
    private Set<SecteurBonusEntity> parseSecteurBonuses(
            String bonusesJson,
            ClassificationEntity classification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SecteurBonusDto> bonusDtos = mapper.readValue(bonusesJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, SecteurBonusDto.class));

            return bonusDtos.stream()
                    .map(dto -> {
                        SecteurBonusEntity bonus = new SecteurBonusEntity();
                        bonus.setClassification(classification);
                        bonus.setSecteur(secteurRepository.findById(dto.getSecteurId())
                                .orElseThrow(() -> new NotFoundException("Secteur not found")));
                        bonus.setBonusPercentage(dto.getBonusPercentage());
                        bonus.setComment(dto.getComment());
                        return bonus;
                    })
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new BadRequestException("Invalid secteur bonuses data: " + e.getMessage());
        }
    }

    private Set<GeographicBonusEntity> parseGeographicBonuses(
            String bonusesJson,
            ClassificationEntity classification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<GeographicBonusDto> bonusDtos = mapper.readValue(bonusesJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, GeographicBonusDto.class));

            return bonusDtos.stream()
                    .map(dto -> {
                        GeographicBonusEntity bonus = new GeographicBonusEntity();
                        bonus.setClassification(classification);
                        bonus.setGeographicCategory(geographicCategoryRepository
                                .findById(dto.getGeographicCategoryId())
                                .orElseThrow(() -> new NotFoundException("Geographic category not found")));
                        bonus.setBonusPercentage(dto.getBonusPercentage());
                        bonus.setComment(dto.getComment());
                        return bonus;
                    })
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new BadRequestException("Invalid geographic bonuses data: " + e.getMessage());
        }
    }

    private String handleFileUpload(MultipartFile file, String classificationCode) {
        try {
            // Get file extension from original filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "";

            // Create new filename with timestamp to ensure uniqueness
            String fileName = String.format("justification_%s_%d.%s",
                    classificationCode,
                    System.currentTimeMillis(),
                    extension);

            Path targetLocation = Path.of(uploadPath, fileName);
            log.info("Attempting to store file at: {}", targetLocation.toAbsolutePath()); // Log the absolute path
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully stored file at: {}", targetLocation.toAbsolutePath());
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

    private ClassificationResponseDto mapToDto(ClassificationEntity entity) {
        ClassificationResponseDto dto = new ClassificationResponseDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setYear(entity.getYear());
        dto.setJustificationPath(entity.getJustificationPath());
        dto.setActive(entity.getActive());

        // Map nomenclature references
        dto.setNomenclatureSecteurId(entity.getNomenclatureSecteur().getId());
        dto.setNomenclatureSecteurTitle(entity.getNomenclatureSecteur().getTitle());
        dto.setNomenclatureGeographicId(entity.getNomenclatureGeographic().getId());
        dto.setNomenclatureGeographicTitle(entity.getNomenclatureGeographic().getTitle());
        dto.setEvaluationGridId(entity.getEvaluationGrid().getId());
        dto.setEvaluationGridTitle(entity.getEvaluationGrid().getName());

        // Map dates
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Map bonuses using the new mapping methods
        dto.setSecteurBonuses(entity.getSecteurBonuses().stream()
                .map(this::mapSecteurBonusToDto)
                .collect(Collectors.toList()));

        dto.setGeographicBonuses(entity.getGeographicBonuses().stream()
                .map(this::mapGeographicBonusToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private SecteurBonusResponseDto mapSecteurBonusToDto(SecteurBonusEntity entity) {
        SecteurBonusResponseDto dto = new SecteurBonusResponseDto();
        dto.setId(entity.getId());
        dto.setSecteurId(entity.getSecteur().getId());
        dto.setSecteurTitle(entity.getSecteur().getTitle());
        dto.setBonusPercentage(entity.getBonusPercentage());
        dto.setComment(entity.getComment());
        return dto;
    }

    private GeographicBonusResponseDto mapGeographicBonusToDto(GeographicBonusEntity entity) {
        GeographicBonusResponseDto dto = new GeographicBonusResponseDto();
        dto.setId(entity.getId());
        dto.setGeographicCategoryId(entity.getGeographicCategory().getId());
        dto.setGeographicCategoryTitle(entity.getGeographicCategory().getTitle());
        dto.setBonusPercentage(entity.getBonusPercentage());
        dto.setComment(entity.getComment());
        return dto;
    }

    private boolean matchesFilters(ClassificationEntity classification, Map<String, String> filters) {
        return filters.entrySet().stream().allMatch(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();

            return switch (key) {
                case "code" -> classification.getCode().toLowerCase().contains(value.toLowerCase());
                case "title" -> classification.getTitle().toLowerCase().contains(value.toLowerCase());
                case "active" -> classification.getActive().toString().equals(value);
                default -> true;
            };
        });
    }
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
}
