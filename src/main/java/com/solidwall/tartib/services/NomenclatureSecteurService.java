package com.solidwall.tartib.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FileStorageException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.nomenclatureSecteur.CreateNomenclatureSecteurDto;
import com.solidwall.tartib.dto.nomenclatureSecteur.UpdateNomenclatureSecteurDto;
import com.solidwall.tartib.dto.nomenclatureSecteur.Secteur.CreateSecteurDto;
import com.solidwall.tartib.dto.nomenclatureSecteur.Secteur.UpdateSecteurDto;

import com.solidwall.tartib.entities.NomenclatureSecteurEntity;
import com.solidwall.tartib.entities.SecteurEntity;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.implementations.NomenclatureSecteurImplementation;
import com.solidwall.tartib.repositories.NomenclatureSecteurRepository;
import com.solidwall.tartib.repositories.SecteurRepository;
import com.solidwall.tartib.repositories.MinisterRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional
@Slf4j
public class NomenclatureSecteurService  implements NomenclatureSecteurImplementation {
    
    @Value("${app.upload.nomenclature-docs}")
    private String uploadPath;
    
    @Autowired
    private NomenclatureSecteurRepository nomenclatureRepository;

    @Autowired
    private SecteurRepository secteurRepository;

    @Autowired
    private MinisterRepository ministerRepository;

    void validateMinisterAssignments(List<SecteurEntity> secteurs) {
        // Create a map to track minister assignments
        Map<Long, String> ministerAssignments = new HashMap<>();
        
        secteurs.stream()
            .flatMap(secteur -> secteur.getMinisters().stream()
            .map(minister -> Map.entry(minister.getId(), secteur.getTitle())))
            .forEach(entry -> {
            String existingSecteur = ministerAssignments.put(entry.getKey(), entry.getValue());
            if (existingSecteur != null) {
                throw new BadRequestException(
                String.format("Minister already assigned to sector '%s'. Cannot assign to '%s'", 
                existingSecteur, entry.getValue())
                );
            }
            });
    }
      List<MinisterEntity> getAvailableMinistersForSector(Long nomenclatureId, Long sectorId) {
        NomenclatureSecteurEntity nomenclature = nomenclatureRepository.findById(nomenclatureId)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
            
        // Get all ministers already assigned in this nomenclature
        Set<Long> assignedMinisterIds = nomenclature.getSecteurs().stream()
            .filter(s -> !s.getId().equals(sectorId)) // Exclude current sector
            .flatMap(s -> s.getMinisters().stream())
            .map(MinisterEntity::getId)
            .collect(Collectors.toSet());
            
        // Return only unassigned ministers
        return ministerRepository.findAll().stream()
            .filter(m -> !assignedMinisterIds.contains(m.getId()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NomenclatureSecteurEntity create(CreateNomenclatureSecteurDto dto) {
        log.info("Creating new nomenclature secteur with code: {}", dto.getCode());
        
        // Create and save the base nomenclature
        NomenclatureSecteurEntity nomenclature = new NomenclatureSecteurEntity();
        nomenclature.setCode(dto.getCode());
        nomenclature.setTitle(dto.getTitle());
        nomenclature.setYear(dto.getYear());
        nomenclature.setActive(false);
        nomenclature.setSecteurs(new ArrayList<>());
        
        // Save first to get ID
        NomenclatureSecteurEntity nomenclatureSaved = nomenclatureRepository.save(nomenclature);
        
        // Parse the JSON string to get sectors list
        List<SecteurEntity> secteurs = dto.getSecteursList().stream()
            .map(secteurDto -> createSecteur(secteurDto, nomenclatureSaved))
            .collect(Collectors.toList());
            
        // Validate minister assignments
        validateMinisterAssignments(secteurs);
        
        nomenclatureSaved.setSecteurs(secteurs);
        
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
    public NomenclatureSecteurEntity update(Long id, UpdateNomenclatureSecteurDto data) {
        log.info("Updating nomenclature secteur with id: {}", id);
        
        NomenclatureSecteurEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
    
        // Update basic nomenclature fields
        nomenclature.setCode(data.getCode());
        nomenclature.setTitle(data.getTitle());
        nomenclature.setYear(data.getYear());
    
        // Get the current sectors in the database
        Set<Long> updatedSectorIds = data.getSecteursList().stream()
            .map(UpdateSecteurDto::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    
        // Instead of removing sectors directly from the collection,
        // we'll mark sectors that should be deleted
        List<SecteurEntity> sectorsToDelete = nomenclature.getSecteurs().stream()
            .filter(sector -> !updatedSectorIds.contains(sector.getId()))
            .collect(Collectors.toList());
    
        // Remove the sectors that aren't in the update list
        for (SecteurEntity sectorToDelete : sectorsToDelete) {
            // Clear the ministers set first to avoid any unintended deletions
            sectorToDelete.getMinisters().clear();
            nomenclature.getSecteurs().remove(sectorToDelete);
            // Explicitly delete the sector
            secteurRepository.delete(sectorToDelete);
        }
    
        // Update existing sectors and add new ones
        for (UpdateSecteurDto sectorDto : data.getSecteursList()) {
            SecteurEntity sector = updateSecteur(sectorDto, nomenclature);
            if (!nomenclature.getSecteurs().contains(sector)) {
                nomenclature.getSecteurs().add(sector);
            }
        }
    
        // Validate minister assignments
        validateMinisterAssignments(nomenclature.getSecteurs());
    
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

    private SecteurEntity createSecteur(CreateSecteurDto dto, NomenclatureSecteurEntity nomenclature) {
        SecteurEntity secteur = new SecteurEntity();
        secteur.setCode(dto.getCode());
        secteur.setTitle(dto.getTitle());
        secteur.setDescription(dto.getDescription());
        secteur.setNomenclature(nomenclature); // Attach the nomenclature to the secteur
        
        // Stream on dto.getMinisterIds() and return a list of MinisterEntity objects
        Set<MinisterEntity> ministers = dto.getMinisterIds().stream()
            .map(id -> ministerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Minister not found with id: " + id)))
            .collect(Collectors.toSet());
        
        secteur.setMinisters(ministers); // Set ministers using the list of MinisterEntity objects
        return secteurRepository.save(secteur);
    }

    private SecteurEntity updateSecteur(UpdateSecteurDto dto, NomenclatureSecteurEntity nomenclature) {
        SecteurEntity secteur;
        if (dto.getId() != null) {
            // Update existing sector
            secteur = secteurRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Secteur not found with id: " + dto.getId()));
        } else {
            // Create new sector
            secteur = new SecteurEntity();
        }
        
        secteur.setCode(dto.getCode());
        secteur.setTitle(dto.getTitle());
        secteur.setDescription(dto.getDescription());
        secteur.setNomenclature(nomenclature);
    
        // Clear existing ministers and set new ones
        // This maintains the independence of the Minister entities
        secteur.getMinisters().clear();
        Set<MinisterEntity> ministers = dto.getMinisterIds().stream()
            .map(id -> ministerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Minister not found with id: " + id)))
            .collect(Collectors.toSet());
        secteur.getMinisters().addAll(ministers);
        
        return secteurRepository.save(secteur);
    }
    @Override
    public NomenclatureSecteurEntity getOne(Long id) {
        log.info("Fetching nomenclature secteur with id: {}", id);
        
        return nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
    }

    @Override
    public List<NomenclatureSecteurEntity> findAll(Map<String, String> filters) {
        log.info("Fetching all nomenclature secteurs with filters: {}", filters);
        
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
        log.info("Activating nomenclature secteur with id: {}", id);
        
        // First deactivate all
        deactivateAll();
        
        // Then activate the selected one
        NomenclatureSecteurEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
        
        nomenclature.setActive(true);
        nomenclatureRepository.save(nomenclature);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting nomenclature secteur with id: {}", id);
        
        NomenclatureSecteurEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));

        // Delete associated file if exists
        deleteFileIfExists(nomenclature.getJustificationPath());
        
        // Delete the entity
        nomenclatureRepository.delete(nomenclature);
    }

    @Override
    public void deactivateAll() {
        log.info("Deactivating all nomenclature secteurs");
        
        List<NomenclatureSecteurEntity> nomenclatures = nomenclatureRepository.findAll();
        nomenclatures.forEach(n -> n.setActive(false));
        nomenclatureRepository.saveAll(nomenclatures);
    }

    @Override
    public NomenclatureSecteurEntity findActive() {
        log.info("Fetching active nomenclature secteur");
        
        return nomenclatureRepository.findAll().stream()
            .filter(NomenclatureSecteurEntity::getActive)
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
    private boolean matchesFilters(NomenclatureSecteurEntity nomenclature, Map<String, String> filters) {
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
    // Add this method to NomenclatureSecteurService.java
public Resource downloadJustificationFile(Long id) {
    try {
        NomenclatureSecteurEntity nomenclature = nomenclatureRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Nomenclature not found"));
        
        if (nomenclature.getJustificationPath() == null || nomenclature.getJustificationPath().isEmpty()) {
            throw new NotFoundException("No justification file found for this nomenclature");
        }
        
        // Create file path from upload directory and stored filename
        Path filePath = Path.of(uploadPath, nomenclature.getJustificationPath());
        Resource resource = new UrlResource(filePath.toUri());
        
        if (!resource.exists() || !resource.isReadable()) {
            throw new NotFoundException("File not found or not readable");
        }
        
        return resource;
    } catch (MalformedURLException e) {
        throw new FileStorageException("Error: " + e.getMessage());
    }
}

// Add this method to determine the content type
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