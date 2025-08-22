package com.solidwall.tartib.controllers;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.projectvalidation.CNAPValidationRequestDto;
import com.solidwall.tartib.dto.projectvalidation.MinistryProjectsDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectValidationDataDto;
import com.solidwall.tartib.dto.projectvalidation.ProjectValidationDetailDto;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.services.ProjectValidationCnapService;
import com.solidwall.tartib.services.ProjectValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/project-validation")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProjectValidationCnapController {

    @Autowired
     private ProjectValidationCnapService projectValidationService;

    /**
     * Get ministries and their projects from latest classification
     */
    @GetMapping("/ministries-projects")
    public ResponseEntity<CustomResponseHelper<List<MinistryProjectsDto>>> getMinistryProjects() {
        log.info("Fetching ministry projects from latest classification");
        
        try {
            List<MinistryProjectsDto> ministryProjects = projectValidationService.getMinistryProjectsFromLatestClassement();
            
            CustomResponseHelper<List<MinistryProjectsDto>> response = CustomResponseHelper.<List<MinistryProjectsDto>>builder()
                .body(ministryProjects)
                .message("Ministry projects retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching ministry projects: {}", e.getMessage());
            
            CustomResponseHelper<List<MinistryProjectsDto>> response = CustomResponseHelper.<List<MinistryProjectsDto>>builder()
                .body(null)
                .message("Error fetching ministry projects: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get validation data for a specific project
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectValidationDataDto>> getProjectValidationData(
            @PathVariable Long projectId) {
        log.info("Fetching validation data for project ID: {}", projectId);
        
        try {
            ProjectValidationDataDto validationData = projectValidationService.getProjectValidationData(projectId);
            
            CustomResponseHelper<ProjectValidationDataDto> response = CustomResponseHelper.<ProjectValidationDataDto>builder()
                .body(validationData)
                .message("Project validation data retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching project validation data: {}", e.getMessage());
            
            CustomResponseHelper<ProjectValidationDataDto> response = CustomResponseHelper.<ProjectValidationDataDto>builder()
                .body(null)
                .message("Error fetching project validation data: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update project CNAP validation status
     */
    @PutMapping("/cnap-validation")
    public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> updateCNAPValidation(
            @RequestBody CNAPValidationRequestDto request) {
        log.info("Updating CNAP validation for project ID: {} to status: {}", 
                 request.getProjectId(), request.getStatus());
        
        try {
            ProjectIdentityEntity updatedProject = projectValidationService.updateProjectCNAPStatus(
                request.getProjectId(), request.getStatus());
            
            CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
                .body(updatedProject)
                .message("Project CNAP status updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid CNAP status: {}", e.getMessage());
            
            CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
                .body(null)
                .message("Invalid CNAP status: " + e.getMessage())
                .error(true)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            log.error("Error updating CNAP validation: {}", e.getMessage());
            
            CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
                .body(null)
                .message("Error updating CNAP validation: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get projects by CNAP status
     */
    @GetMapping("/cnap-status/{status}")
    public ResponseEntity<CustomResponseHelper<List<ProjectValidationDetailDto>>> getProjectsByCNAPStatus(
            @PathVariable String status) {
        log.info("Fetching projects with CNAP status: {}", status);
        
        try {
            List<ProjectValidationDetailDto> projects = projectValidationService.getProjectsByCNAPStatus(status);
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(projects)
                .message("Projects with CNAP status retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid CNAP status: {}", e.getMessage());
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(null)
                .message("Invalid CNAP status: " + e.getMessage())
                .error(true)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            log.error("Error fetching projects by CNAP status: {}", e.getMessage());
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(null)
                .message("Error fetching projects by CNAP status: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Validate project (status: valide_par_cnap)
     */
    @PutMapping("/validate/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> validateProject(
            @PathVariable Long projectId) {
        log.info("Validating project ID: {}", projectId);
        
        CNAPValidationRequestDto request = CNAPValidationRequestDto.builder()
            .projectId(projectId)
            .status("valide_par_cnap")
            .build();
            
        return updateCNAPValidation(request);
    }

    /**
     * Validate project with reserve (status: valide_avec_reserve)
     */
    @PutMapping("/validate-with-reserve/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> validateProjectWithReserve(
            @PathVariable Long projectId) {
        log.info("Validating project with reserve - ID: {}", projectId);
        
        CNAPValidationRequestDto request = CNAPValidationRequestDto.builder()
            .projectId(projectId)
            .status("valide_avec_reserve")
            .build();
            
        return updateCNAPValidation(request);
    }

    /**
     * Reject project (status: rejete_par_cnap)
     */
    @PutMapping("/reject/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> rejectProject(
            @PathVariable Long projectId) {
        log.info("Rejecting project ID: {}", projectId);
        
        CNAPValidationRequestDto request = CNAPValidationRequestDto.builder()
            .projectId(projectId)
            .status("rejete_par_cnap")
            .build();
            
        return updateCNAPValidation(request);
    }

    /**
     * Get all projects with any CNAP validation status
     */
    @GetMapping("/cnap-validated-projects")
    public ResponseEntity<CustomResponseHelper<List<ProjectValidationDetailDto>>> getAllCNAPValidatedProjects() {
        log.info("Fetching all CNAP validated projects");
        
        try {
            // Get projects with all CNAP statuses
            List<ProjectValidationDetailDto> validatedProjects = projectValidationService.getProjectsByCNAPStatus("valide_par_cnap");
            List<ProjectValidationDetailDto> reserveProjects = projectValidationService.getProjectsByCNAPStatus("valide_avec_reserve");
            List<ProjectValidationDetailDto> rejectedProjects = projectValidationService.getProjectsByCNAPStatus("rejete_par_cnap");
            
            // Combine all lists
            validatedProjects.addAll(reserveProjects);
            validatedProjects.addAll(rejectedProjects);
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(validatedProjects)
                .message("All CNAP validated projects retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching CNAP validated projects: {}", e.getMessage());
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(null)
                .message("Error fetching CNAP validated projects: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
     @GetMapping("/projects/ministry/{ministryId}")
    public ResponseEntity<CustomResponseHelper<List<ProjectValidationDetailDto>>> getProjectsByMinistry(
            @PathVariable Long ministryId) {
        log.info("Fetching projects from latest classement for ministry ID: {}", ministryId);
        
        try {
            List<ProjectValidationDetailDto> projects = projectValidationService.getProjectsByMinistry(ministryId);
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(projects)
                .message("Projects for ministry retrieved successfully from latest classement")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching projects for ministry {}: {}", ministryId, e.getMessage());
            
            CustomResponseHelper<List<ProjectValidationDetailDto>> response = 
                CustomResponseHelper.<List<ProjectValidationDetailDto>>builder()
                .body(null)
                .message("Error fetching projects for ministry: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //rollback project to evaluated status
    @PutMapping("/rollback/{projectIdentityId}")
    public ResponseEntity<CustomResponseHelper<ProjectIdentityEntity>> rollbackProjectToEvaluatedStatus(
            @PathVariable Long projectIdentityId) {
        log.info("Rolling back project ID: {} to evaluated status", projectIdentityId);
        
        try {
            ProjectIdentityEntity rolledBackProject = projectValidationService.rollbackToEvaluatedStatus(projectIdentityId);
            
            CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
                .body(rolledBackProject)
                .message("Project rolled back to evaluated status successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error rolling back project: {}", e.getMessage());
            
            CustomResponseHelper<ProjectIdentityEntity> response = CustomResponseHelper.<ProjectIdentityEntity>builder()
                .body(null)
                .message("Error rolling back project: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}