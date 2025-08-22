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
import com.solidwall.tartib.dto.adminevaluation.request.CreateAdminEvaluationDto;
import com.solidwall.tartib.dto.adminevaluation.response.AdminEvaluationResponseDto;
import com.solidwall.tartib.implementations.AdminEvaluationImplementation;
import com.solidwall.tartib.repositories.UserRepository;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

@RestController
@RequestMapping("admin-evaluation")
public class AdminEvaluationController {

    @Autowired
    private AdminEvaluationImplementation adminEvaluationImplementation;
    @Autowired
    AuthenticationFacade authenticationFacade;

    

    @Autowired
    private NotificationService notificationService;

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<AdminEvaluationResponseDto>> create(
            @RequestBody CreateAdminEvaluationDto data) {
        CustomResponseHelper<AdminEvaluationResponseDto> response = CustomResponseHelper
                .<AdminEvaluationResponseDto>builder()
                .body(adminEvaluationImplementation.create(data))
                .message("Admin evaluation created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{projectId}")
    public ResponseEntity<CustomResponseHelper<AdminEvaluationResponseDto>> update(
            @PathVariable Long projectId,
            @RequestBody CreateAdminEvaluationDto data) {
        CustomResponseHelper<AdminEvaluationResponseDto> response = CustomResponseHelper
                .<AdminEvaluationResponseDto>builder()
                .body(adminEvaluationImplementation.update(projectId, data))
                .message("Admin evaluation updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{projectId}")
    public ResponseEntity<CustomResponseHelper<AdminEvaluationResponseDto>> getByProjectId(
            @PathVariable Long projectId) {
        CustomResponseHelper<AdminEvaluationResponseDto> response = CustomResponseHelper
                .<AdminEvaluationResponseDto>builder()
                .body(adminEvaluationImplementation.getByProjectId(projectId))
                .message("Admin evaluation retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("getById/{id}")

    public ResponseEntity<CustomResponseHelper<AdminEvaluationResponseDto>> getById(
            @PathVariable Long id) {
        CustomResponseHelper<AdminEvaluationResponseDto> response = CustomResponseHelper
                .<AdminEvaluationResponseDto>builder()
                .body(adminEvaluationImplementation.getById(id))
                .message("Admin evaluation retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long projectId) {
        adminEvaluationImplementation.delete(projectId);

        CustomResponseHelper<Void> response = CustomResponseHelper
                .<Void>builder()
                .message("Admin evaluation deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    // ACCES NEEDED CHECK
    @PutMapping("validate/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> validateAutoEvaluation(@PathVariable Long projectId) {
        long getCurrentUserId = authenticationFacade.getCurrentUserId();
        adminEvaluationImplementation.validateAdminEvaluation(projectId);

        String message = " Admin-evaluation validated successfully for project ID: "
                + projectId;
        notificationService.buildAndSendNotification(getCurrentUserId,message, "centrale_evaluation_notif" );

        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Admin-evaluation validated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ACCES NEEDED CHECK
    @PutMapping("validate-final/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> validateFinalAutoEvaluation(@PathVariable Long projectId) {
        long getCurrentUserId = authenticationFacade.getCurrentUserId();
        adminEvaluationImplementation.validateFinalAdminEvaluation(projectId);

        String message = " Final-admin-evaluation validated successfully for project ID: "
                + projectId;
         notificationService.buildAndSendNotification(getCurrentUserId,message, "centrale_evaluation_notif" );
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("final-Admin-evaluation validated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
