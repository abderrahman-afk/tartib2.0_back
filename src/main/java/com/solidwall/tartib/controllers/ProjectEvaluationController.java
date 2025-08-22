package com.solidwall.tartib.controllers;

import java.util.Date;

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
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.projectevaluation.request.CreateProjectEvaluationDto;
import com.solidwall.tartib.dto.projectevaluation.response.ProjectEvaluationResponseDto;
import com.solidwall.tartib.entities.PonderationEntity;
import com.solidwall.tartib.implementations.ProjectEvaluationImplementation;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

@RestController
@RequestMapping("project-evaluation")
public class ProjectEvaluationController {

    @Autowired
    private ProjectEvaluationImplementation projectEvaluationImplementation;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<ProjectEvaluationResponseDto>> create(
            @RequestBody CreateProjectEvaluationDto data) {
        CustomResponseHelper<ProjectEvaluationResponseDto> response = CustomResponseHelper
                .<ProjectEvaluationResponseDto>builder()
                .body(projectEvaluationImplementation.create(data))
                .message("Project evaluation created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectEvaluationResponseDto>> update(
            @PathVariable Long projectId,
            @RequestBody CreateProjectEvaluationDto data) {
        CustomResponseHelper<ProjectEvaluationResponseDto> response = CustomResponseHelper
                .<ProjectEvaluationResponseDto>builder()
                .body(projectEvaluationImplementation.update(projectId, data))
                .message("Project evaluation updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{projectId}")
    public ResponseEntity<CustomResponseHelper<ProjectEvaluationResponseDto>> getOne(
            @PathVariable Long projectId) {
        CustomResponseHelper<ProjectEvaluationResponseDto> response = CustomResponseHelper
                .<ProjectEvaluationResponseDto>builder()
                .body(projectEvaluationImplementation.getByProjectId(projectId))
                .message("Project evaluation retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("checkForPonderation/{evaluationId}")
    public ResponseEntity<CustomResponseHelper<PonderationEntity>> CheckPonderation(
            @PathVariable Long evaluationId) {
        CustomResponseHelper<PonderationEntity> response = CustomResponseHelper
                .<PonderationEntity>builder()
                .body(projectEvaluationImplementation.checkForPonderation(evaluationId))
                .message("Project evaluation retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long projectId) {
        projectEvaluationImplementation.delete(projectId);
        CustomResponseHelper<Void> response = CustomResponseHelper
                .<Void>builder()
                .message("Project evaluation deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // // ACCES NEEDED CHECK
    @PutMapping("validate/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> validateAutoEvaluation(@PathVariable Long projectId) {

        long getCurrentUserId = authenticationFacade.getCurrentUserId();
        projectEvaluationImplementation.validateAutoEvaluation(projectId);
        String message = "Auto-evaluation validated successfully "
                + projectEvaluationImplementation.getByProjectId(projectId).getProjectName();

        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Auto-evaluation validated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        notificationService.buildAndSendNotification(getCurrentUserId, message, "auto_evaluation_notif");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // // ACCES NEEDED CHECK

    @PutMapping("validate-final/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> validateFinalAutoEvaluation(@PathVariable Long projectId) {
        projectEvaluationImplementation.validateFinalAutoEvaluation(projectId);
        long getCurrentUserId = authenticationFacade.getCurrentUserId();
        String message = "Final auto-evaluation validated successfully "
                + projectEvaluationImplementation.getByProjectId(projectId).getProjectName();
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Auto-evaluation validated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        notificationService.buildAndSendNotification(getCurrentUserId, message, "auto_evaluation_notif");

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}