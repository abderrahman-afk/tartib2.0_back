package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.user.CreateForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ForgotPasswordRequestDto;
import com.solidwall.tartib.dto.user.ProcessForgotPasswordRequestDto;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.enums.ForgotPasswordStatus;
import com.solidwall.tartib.implementations.ForgotPasswordImplementation;
import com.solidwall.tartib.repositories.UserRepository;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("forgot-password")
public class ForgotPasswordController {
        @Autowired
        AuthenticationFacade authenticationFacade;

        @Autowired
        private NotificationService notificationService;
        @Autowired
        private ForgotPasswordImplementation forgotPasswordImplementation;

        @Autowired
        private UserRepository userRepository;

        @PostMapping("verify-email")
        public ResponseEntity<CustomResponseHelper<UserEntity>> verifyEmail(@RequestParam String email) {
                UserEntity user = forgotPasswordImplementation.verifyEmailAndGetUser(email);

                CustomResponseHelper<UserEntity> response = CustomResponseHelper.<UserEntity>builder()
                                .body(user)
                                .message("Email verified successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }

        // notification to be added
        @PostMapping("request")
        public ResponseEntity<CustomResponseHelper<ForgotPasswordRequestDto>> createRequest(
                        @Valid @RequestBody CreateForgotPasswordRequestDto data) {
                 UserEntity  user = userRepository.findByEmail(data.getEmail()).get();

                ForgotPasswordRequestDto request = forgotPasswordImplementation
                                .createForgotPasswordRequest(data);

                String message = "demande changement de mot de passe pour l'utilisateur avec l'email "
                                + data.getEmail();
                notificationService.buildAndSendNotification(user.getId(), message,
                                "user_demande_pass");
                                System.out.println(message + " " + user.getId());
                CustomResponseHelper<ForgotPasswordRequestDto> response = CustomResponseHelper
                                .<ForgotPasswordRequestDto>builder()
                                .body(request)
                                .message("Password reset request created successfully")
                                .error(false)
                                .status(HttpStatus.CREATED.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }

        @GetMapping("requests")
        public ResponseEntity<CustomResponseHelper<List<ForgotPasswordRequestDto>>> getAllRequests() {
                List<ForgotPasswordRequestDto> requests = forgotPasswordImplementation.getAllRequests();

                CustomResponseHelper<List<ForgotPasswordRequestDto>> response = CustomResponseHelper
                                .<List<ForgotPasswordRequestDto>>builder()
                                .body(requests)
                                .message("Forgot password requests retrieved successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("requests/status/{status}")
        public ResponseEntity<CustomResponseHelper<List<ForgotPasswordRequestDto>>> getRequestsByStatus(
                        @PathVariable ForgotPasswordStatus status) {

                List<ForgotPasswordRequestDto> requests = forgotPasswordImplementation.getRequestsByStatus(status);

                CustomResponseHelper<List<ForgotPasswordRequestDto>> response = CustomResponseHelper
                                .<List<ForgotPasswordRequestDto>>builder()
                                .body(requests)
                                .message("Requests retrieved successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("requests/{id}")
        public ResponseEntity<CustomResponseHelper<ForgotPasswordRequestDto>> getRequestById(@PathVariable Long id) {
                ForgotPasswordRequestDto request = forgotPasswordImplementation.getRequestById(id);

                CustomResponseHelper<ForgotPasswordRequestDto> response = CustomResponseHelper
                                .<ForgotPasswordRequestDto>builder()
                                .body(request)
                                .message("Request retrieved successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }

        // notification to be added
        @PutMapping("requests/{id}/process")
        public ResponseEntity<CustomResponseHelper<ForgotPasswordRequestDto>> processRequest(
                        @PathVariable Long id,
                        @Valid @RequestBody ProcessForgotPasswordRequestDto data) {
                long getCurrentUserId = authenticationFacade.getCurrentUserId();

                String message = "demande changement de mot de passe effectuée ";
                notificationService.buildAndSendNotification(getCurrentUserId, message,
                                "user_demande_pass");
                ForgotPasswordRequestDto processedRequest = forgotPasswordImplementation.processRequest(id, data);

                CustomResponseHelper<ForgotPasswordRequestDto> response = CustomResponseHelper
                                .<ForgotPasswordRequestDto>builder()
                                .body(processedRequest)
                                .message("Request processed successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("user-requests")
        public ResponseEntity<CustomResponseHelper<List<ForgotPasswordRequestDto>>> getUserRequests(
                        @RequestParam String email) {

                List<ForgotPasswordRequestDto> requests = forgotPasswordImplementation.getUserRequests(email);

                CustomResponseHelper<List<ForgotPasswordRequestDto>> response = CustomResponseHelper
                                .<List<ForgotPasswordRequestDto>>builder()
                                .body(requests)
                                .message("User requests retrieved successfully")
                                .error(false)
                                .status(HttpStatus.OK.value())
                                .timestamp(new Date())
                                .build();

                return ResponseEntity.ok(response);
        }
}