package com.solidwall.tartib.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FileStorageException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.nomenclatureGeographic.CreateNomenclatureGeographicDto;
import com.solidwall.tartib.dto.nomenclatureGeographic.UpdateNomenclatureGeographicDto;
import com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory.CreateGeographicCategoryDto;
import com.solidwall.tartib.dto.nomenclatureGeographic.GeographicCategory.UpdateGeographicCategoryDto;
import com.solidwall.tartib.entities.GeographicCategoryEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;
import com.solidwall.tartib.implementations.NomenclatureGeographicImplementation;
import com.solidwall.tartib.repositories.GeographicCategoryRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;
import com.solidwall.tartib.repositories.NomenclatureGeographicRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional
@Slf4j
public class NomenclatureGeographicService  implements NomenclatureGeographicImplementation {
    
    @Value("${app.upload.nomenclature-docs}")
    private String uploadPath;
    
    @Autowired
    private NomenclatureGeographicRepository nomenclatureRepository;

    @Autowired
    private GeographicCategoryRepository geographicCategoryRepository;

    @Autowired
    private GovernorateRepository governorateRepository;

    void validateGovernoratsAssignments(List<GeographicCategoryEntity> geographicCategories) {
        // Create a map to track governorats assignments
        Map<Long, String> governoratsAssignments = new HashMap<>();
        
        geographicCategories.stream()
            .flatMap(geographicCategory -> geographicCategory.getGovernorates().stream()
            .map(governorate -> Map.entry(governorate.getId(), governorate.getName())))
            .forEach(entry -> {
            String existingGovernorate = governoratsAssignments.put(entry.getKey(), entry.getValue());
            if (existingGovernorate != null) {
                throw new BadRequestException(
                String.format("Governorate already assigned to category '%s'. Cannot assign to '%s'", 
                existingGovernorate, entry.getValue())
                );
            }
            });
    }
      List<GovernorateEntity> getAvailableGovernorateForGeoCategory(Long nomenclatureId, Long geoCategoryId) {
        NomenclatureGeographicEntity nomenclature = nomenclatureRepository.findById(nomenclatureId)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
            
        // Get all goverorants already assigned in this nomenclature
        Set<Long> assignedGovernoratIds = nomenclature.getGeographicCategories().stream()
            .filter(s -> !s.getId().equals(geoCategoryId)) // Exclude current  
            .flatMap(s -> s.getGovernorates().stream())
            .map(GovernorateEntity::getId)
            .collect(Collectors.toSet());
            
        // Return only unassigned goverorants
        return governorateRepository.findAll().stream()
            .filter(m -> !assignedGovernoratIds.contains(m.getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NomenclatureGeographicEntity create(CreateNomenclatureGeographicDto dto) {
        log.info("Creating new nomenclature geographic with code: {}", dto.getCode());
        
        // Create and save the base nomenclature
        NomenclatureGeographicEntity nomenclature = new NomenclatureGeographicEntity();
        nomenclature.setCode(dto.getCode());
        nomenclature.setTitle(dto.getTitle());
        nomenclature.setYear(dto.getYear());
        nomenclature.setActive(false);
        nomenclature.setGeographicCategories(new ArrayList<>());
        
        // Save first to get ID
        NomenclatureGeographicEntity nomenclatureSaved = nomenclatureRepository.save(nomenclature);
        
        // Parse the JSON string to get geographic Categories list
        List<GeographicCategoryEntity> geographicCategories = dto.getGeographicCategoriesList().stream()
            .map(geographicCategoriyDto -> createGeographicCategory(geographicCategoriyDto, nomenclatureSaved))
            .collect(Collectors.toList());
            
        // Validate governorats assignments
        validateGovernoratsAssignments(geographicCategories);
        
        nomenclatureSaved.setGeographicCategories(geographicCategories);
        
        // Handle file upload if present
        if (dto.getJustificationFile() != null) {
            String filePath = handleFileUpload(
                dto.getJustificationFile(), 
                nomenclatureSaved.getCode(), 
                nomenclatureSaved.getYear()
            );
            nomenclatureSaved.setJustificationPath(filePath);
        }
        
        return nomenclatureRepository.save(nomenclatureSaved);
    }

    @Override
    @Transactional
    public NomenclatureGeographicEntity update(Long id, UpdateNomenclatureGeographicDto data) {
        log.info("Updating nomenclature geographic with id: {}", id);
        
        NomenclatureGeographicEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
    
        // Update basic nomenclature fields
        nomenclature.setCode(data.getCode());
        nomenclature.setTitle(data.getTitle());
        nomenclature.setYear(data.getYear());
    
        // Get the current geo category in the database
        Set<Long> updatedGeographicCategoryIds = data.getGeographicCategoryList().stream()
            .map(UpdateGeographicCategoryDto::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    
        // Instead of removing   directly from the collection,
         List<GeographicCategoryEntity> GeographicCategoriesToDelete = nomenclature.getGeographicCategories().stream()
            .filter(GeographicCategory -> !updatedGeographicCategoryIds.contains(GeographicCategory.getId()))
            .collect(Collectors.toList());
    
        // Remove the   that aren't in the update list
        for (GeographicCategoryEntity  GeographicCategoryToDelete : GeographicCategoriesToDelete) {
            // Clear the governorats set first to avoid any unintended deletions
            GeographicCategoryToDelete.getGovernorates().clear();
            nomenclature.getGeographicCategories().remove(GeographicCategoryToDelete);
            // Explicitly delete the  
            geographicCategoryRepository.delete(GeographicCategoryToDelete);
        }
    
        // Update existing   and add new ones
        for (UpdateGeographicCategoryDto updateGeographicCategoryDto : data.getGeographicCategoryList()) {
            GeographicCategoryEntity geographicCategoryEntity = updateGeographicCategory(updateGeographicCategoryDto, nomenclature);
            if (!nomenclature.getGeographicCategories().contains(geographicCategoryEntity)) {
                nomenclature.getGeographicCategories().add(geographicCategoryEntity);
            }
        }
    
        // Validate governorats assignments
        validateGovernoratsAssignments(nomenclature.getGeographicCategories());
    
        // Handle file update if new file is provided
        if (data.getJustificationFile() != null) {
            deleteFileIfExists(nomenclature.getJustificationPath());
            String filePath = handleFileUpload(
                data.getJustificationFile(), 
                nomenclature.getCode(), 
                nomenclature.getYear()
            );
            nomenclature.setJustificationPath(filePath);
        }
    
        return nomenclatureRepository.save(nomenclature);
    }

    private GeographicCategoryEntity createGeographicCategory(CreateGeographicCategoryDto dto, NomenclatureGeographicEntity nomenclature) {
        GeographicCategoryEntity geographicCategory = new GeographicCategoryEntity();
        geographicCategory.setCode(dto.getCode());
        geographicCategory.setTitle(dto.getTitle());
        geographicCategory.setDescription(dto.getDescription());
        geographicCategory.setNomenclature(nomenclature); // Attach the nomenclature to the geographic category
        
        // Stream on dto.getGovernoratsrIds() and return a list of governoratsEntities objects
        Set<GovernorateEntity> governorats = dto.getGouvernoratIds().stream()
            .map(id -> governorateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Governorat not found with id: " + id)))
            .collect(Collectors.toSet());
        
            geographicCategory.setGovernorates(governorats); // Set governorats using the list of governoratEntity objects
        return geographicCategoryRepository.save(geographicCategory);
    }

    private GeographicCategoryEntity updateGeographicCategory(UpdateGeographicCategoryDto dto, NomenclatureGeographicEntity nomenclature) {
        GeographicCategoryEntity geographicCategoryEntity;
        if (dto.getId() != null) {
            // Update existing  
            geographicCategoryEntity = geographicCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("geographic Category not found with id: " + dto.getId()));
        } else {
            // Create new  
            geographicCategoryEntity = new GeographicCategoryEntity();
        }
        
        geographicCategoryEntity.setCode(dto.getCode());
        geographicCategoryEntity.setTitle(dto.getTitle());
        geographicCategoryEntity.setDescription(dto.getDescription());
        geographicCategoryEntity.setNomenclature(nomenclature);
    
        // Clear existing governorats and set new ones
        // This maintains the independence of the governorat entities
        geographicCategoryEntity.getGovernorates().clear();
        Set<GovernorateEntity> governorateEntities = dto.getGouvernoratIds().stream()
            .map(id -> governorateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("governorate not found with id: " + id)))
            .collect(Collectors.toSet());
            geographicCategoryEntity.getGovernorates().addAll(governorateEntities);
        
        return geographicCategoryRepository.save(geographicCategoryEntity);
    }
    @Override
    public NomenclatureGeographicEntity getOne(Long id) {
        log.info("Fetching nomenclature geographic category with id: {}", id);
        
        return nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
    }

    @Override
    public List<NomenclatureGeographicEntity> findAll(Map<String, String> filters) {
        log.info("Fetching all nomenclature geographic cateogires with filters: {}", filters);
        
        // If no filters, return all
        if (filters == null || filters.isEmpty()) {
            return nomenclatureRepository.findAll().stream()
                .collect(Collectors.toList());
        }

        // Apply filters if present
        return nomenclatureRepository.findAll().stream()
            .filter(nomenclature -> matchesFilters(nomenclature, filters))
            .collect(Collectors.toList());
    }

    @Override
    public void activate(Long id) {
        log.info("Activating nomenclature geographic with id: {}", id);
        
        // First deactivate all
        deactivateAll();
        
        // Then activate the selected one
        NomenclatureGeographicEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
        
        nomenclature.setActive(true);
        nomenclatureRepository.save(nomenclature);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting nomenclature geographic with id: {}", id);
        
        NomenclatureGeographicEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));

        // Delete associated file if exists
        deleteFileIfExists(nomenclature.getJustificationPath());
        
        // Delete the entity
        nomenclatureRepository.delete(nomenclature);
    }

    @Override
    public void deactivateAll() {
        log.info("Deactivating all nomenclatures geographics");
        
        List<NomenclatureGeographicEntity> nomenclatures = nomenclatureRepository.findAll();
        nomenclatures.forEach(n -> n.setActive(false));
        nomenclatureRepository.saveAll(nomenclatures);
    }

    @Override
    public NomenclatureGeographicEntity findActive() {
        log.info("Fetching active nomenclatures geographics");
        
        return nomenclatureRepository.findAll().stream()
            .filter(NomenclatureGeographicEntity::getActive)
            .findFirst()
            .orElse(null);
    }

    // Helper methods for file handling
    private String handleFileUpload(MultipartFile file, String nomenclatureCode, Integer year) {
        try {
            // Get file extension from original filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            
            // Create new filename: justification_CODE_YEAR.extension
            String fileName = String.format("justification_%s_%d%s", nomenclatureCode, year, extension);
            
            // Create target path
            Path targetLocation = Path.of(uploadPath, fileName);
            
            // Copy file with overwrite if exists
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file", ex);
        }
    }

    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path fileToDelete = Path.of(uploadPath, filePath);
                Files.deleteIfExists(fileToDelete);
            } catch (IOException ex) {
                log.error("Error deleting file: {}", filePath, ex);
            }
        }
    }

    // Helper method to map entity to DTO
  

    // Helper method for filtering
    private boolean matchesFilters(NomenclatureGeographicEntity nomenclature, Map<String, String> filters) {
        return filters.entrySet().stream().allMatch(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            
            return switch (key) {
                case "year" -> nomenclature.getYear().toString().equals(value);
                case "code" -> nomenclature.getCode().toLowerCase().contains(value.toLowerCase());
                case "title" -> nomenclature.getTitle().toLowerCase().contains(value.toLowerCase());
                case "active" -> nomenclature.getActive().toString().equals(value);
                default -> true;
            };
        });
    }
}