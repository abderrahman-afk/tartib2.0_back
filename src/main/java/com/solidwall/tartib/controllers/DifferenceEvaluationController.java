package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.differenceevaluation.CreateDifferenceEvaluationDto;
import com.solidwall.tartib.dto.differenceevaluation.DifferenceEvaluationResponseDto;
import com.solidwall.tartib.implementations.DifferenceEvaluationImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

@RestController
@RequestMapping("difference-evaluation")
public class DifferenceEvaluationController {
    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DifferenceEvaluationImplementation differenceEvaluationService;

    // Initialize differences for a project
    @PostMapping("initialize/{projectId}")
    public ResponseEntity<CustomResponseHelper<List<DifferenceEvaluationResponseDto>>> initializeDifferences(
            @PathVariable Long projectId) {
        CustomResponseHelper<List<DifferenceEvaluationResponseDto>> response = CustomResponseHelper
                .<List<DifferenceEvaluationResponseDto>>builder()
                .body(differenceEvaluationService.recalculateDifferences(projectId))
                .message("Differences calculated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Get all differences for a project
    @GetMapping("project/{projectId}")
    public ResponseEntity<CustomResponseHelper<List<DifferenceEvaluationResponseDto>>> getDifferencesByProject(
            @PathVariable Long projectId) {
        CustomResponseHelper<List<DifferenceEvaluationResponseDto>> response = CustomResponseHelper
                .<List<DifferenceEvaluationResponseDto>>builder()
                .body(differenceEvaluationService.findByProjectEvaluation(projectId))
                .message("Differences retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("differences/projects")
    public ResponseEntity<CustomResponseHelper<List<Long>>> getContradicoryProjects() {
        CustomResponseHelper<List<Long>> response = CustomResponseHelper
                .<List<Long>>builder()
                .body(differenceEvaluationService.findProjectsWithDifferences())
                .message("Differences retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {

        differenceEvaluationService.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("contradictions deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    // // ACCES NEEDED CHECK

    // Update difference status
    @PutMapping("status/{id}")
    public ResponseEntity<CustomResponseHelper<DifferenceEvaluationResponseDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        long getCurrentUserId = authenticationFacade.getCurrentUserId();

        String message = "changement dans la phase contradictoire  " + id;
        notificationService.buildAndSendNotification(getCurrentUserId, message,
                "auto_evaluation_phase_contradictoire_notif");
        CustomResponseHelper<DifferenceEvaluationResponseDto> response = CustomResponseHelper
                .<DifferenceEvaluationResponseDto>builder()
                .body(differenceEvaluationService.updateStatus(id, status))
                .message("Status updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Update remarks
    @PutMapping("{id}/remarks")
    public ResponseEntity<CustomResponseHelper<DifferenceEvaluationResponseDto>> updateRemarks(
            @PathVariable Long id,
            @RequestBody CreateDifferenceEvaluationDto data) {
        CustomResponseHelper<DifferenceEvaluationResponseDto> response = CustomResponseHelper
                .<DifferenceEvaluationResponseDto>builder()
                .body(differenceEvaluationService.update(id, data))
                .message("Remarks updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}