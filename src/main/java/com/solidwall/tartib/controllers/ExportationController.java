package com.solidwall.tartib.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.exportation.AdminEvaluationScoresToExportDto;
import com.solidwall.tartib.dto.exportation.ClassificationResultsExportDto;
import com.solidwall.tartib.dto.exportation.ProjectDetailsToExportDto;
import com.solidwall.tartib.dto.exportation.ProjectToExportDto;
import com.solidwall.tartib.implementations.ExportationImplementation;
import com.solidwall.tartib.services.PdfGenerationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("exportation")
@Slf4j
public class ExportationController {

    @Autowired
    private ExportationImplementation exportationImplementation;
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("project/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectToExportDto>> exportProject(@PathVariable Long projectId) {
        try {
            ProjectToExportDto exportData = exportationImplementation.exportProjectData(projectId);

            CustomResponseHelper<ProjectToExportDto> response = CustomResponseHelper
                    .<ProjectToExportDto>builder()
                    .body(exportData)
                    .message("Project export data retrieved successfully")
                    .error(false)
                    .status(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (Exception e) {
            CustomResponseHelper<ProjectToExportDto> response = CustomResponseHelper
                    .<ProjectToExportDto>builder()
                    .body(null)
                    .message("Error exporting project data: " + e.getMessage())
                    .error(true)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @GetMapping("project/{projectId}/pdf")
    public ResponseEntity<byte[]> exportProjectPdf(@PathVariable Long projectId) {
        try {
            ProjectToExportDto exportData = exportationImplementation.exportProjectData(projectId);
            byte[] pdfBytes = pdfGenerationService.generateProjectPdf(exportData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("project-" + exportData.getProjectCode() + ".pdf").build());

            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }
 @GetMapping("admin-evaluation/{projectId}")
    public ResponseEntity<CustomResponseHelper<AdminEvaluationScoresToExportDto>> exportAdminEvaluation(@PathVariable Long projectId) {
        try {
            AdminEvaluationScoresToExportDto exportData = exportationImplementation.exportAdminEvaluationScores(projectId);
            
            CustomResponseHelper<AdminEvaluationScoresToExportDto> response = CustomResponseHelper
                .<AdminEvaluationScoresToExportDto>builder()
                .body(exportData)
                .message("Admin evaluation export data retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(response.getStatus()).body(response);
            
        } catch (Exception e) {
            CustomResponseHelper<AdminEvaluationScoresToExportDto> response = CustomResponseHelper
                .<AdminEvaluationScoresToExportDto>builder()
                .body(null)
                .message("Error exporting admin evaluation data: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
                
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @GetMapping("admin-evaluation/{projectId}/pdf")
    public ResponseEntity<byte[]> exportAdminEvaluationPdf(@PathVariable Long projectId) {
        try {
            // Get evaluation data
            AdminEvaluationScoresToExportDto exportData = exportationImplementation.exportAdminEvaluationScores(projectId);
            
            // Generate PDF
            byte[] pdfContent = pdfGenerationService.generateAdminEvaluationPdf(exportData);
            
            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                String.format("evaluation_%s_%s.pdf", exportData.getProjectCode(), 
                System.currentTimeMillis()));
            headers.setContentLength(pdfContent.length);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
                
        } catch (Exception e) {
            // Return error response as JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("admin-evaluation/{projectId}/pdf/preview")
    public ResponseEntity<byte[]> previewAdminEvaluationPdf(@PathVariable Long projectId) {
        try {
            // Get evaluation data
            AdminEvaluationScoresToExportDto exportData = exportationImplementation.exportAdminEvaluationScores(projectId);
            
            // Generate PDF
            byte[] pdfContent = pdfGenerationService.generateAdminEvaluationPdf(exportData);
            
            // Prepare response headers for preview (inline display)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", 
                String.format("evaluation_%s_preview.pdf", exportData.getProjectCode()));
            headers.setContentLength(pdfContent.length);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
                
        } catch (Exception e) {
            // Return error response as JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(("Error generating PDF preview: " + e.getMessage()).getBytes());
        }
    }
    

    // classificaiton 
    // Add this method to your ExportationController.java

    @GetMapping("/all-projects-details")
    public ResponseEntity<CustomResponseHelper<List<ProjectDetailsToExportDto>>> exportAllProjectsDetails(@RequestParam(required = false) Integer year) {
        log.info("REST request to export all projects details");
        try {
            List<ProjectDetailsToExportDto> exportData = exportationImplementation.exportAllProjectsDetails(year);
            CustomResponseHelper<List<ProjectDetailsToExportDto>> response = CustomResponseHelper
                .<List<ProjectDetailsToExportDto>>builder()
                .body(exportData)
                .message("All projects details exported successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
            log.info("Successfully exported all projects details");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error exporting all projects details: {}", e.getMessage());
            CustomResponseHelper<List<ProjectDetailsToExportDto>> errorResponse = CustomResponseHelper
                .<List<ProjectDetailsToExportDto>>builder()
                .body(null)
                .message("Failed to export all projects details: " + e.getMessage())
                .error(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/classification-results")
    public ResponseEntity<CustomResponseHelper<ClassificationResultsExportDto>> exportClassificationResults() {
    
        log.info("REST request to export latest classification results");
    
    try {
        ClassificationResultsExportDto exportData = exportationImplementation.exportClassificationResults();
        
        CustomResponseHelper<ClassificationResultsExportDto> response = CustomResponseHelper
            .<ClassificationResultsExportDto>builder()
            .body(exportData)
            .message("Classification results exported successfully")
            .error(false)
            .status(HttpStatus.OK.value())
            .timestamp(new Date())
            .build();

            log.info("Successfully exported classification results with {} projects",
                exportData.getProjects().size());

        return ResponseEntity.ok(response);
        
    } catch (RuntimeException e) {
        log.error("Error exporting classification results: {}", e.getMessage());
        
        CustomResponseHelper<ClassificationResultsExportDto> errorResponse = CustomResponseHelper
            .<ClassificationResultsExportDto>builder()
            .body(null)
            .message("Failed to export classification results: " + e.getMessage())
            .error(true)
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(new Date())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

}
