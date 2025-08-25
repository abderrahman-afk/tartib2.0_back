package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.autorisation.CreateDto;
import com.solidwall.tartib.dto.autorisation.UpdateDto;
import com.solidwall.tartib.entities.AutorisationEntity;
import com.solidwall.tartib.implementations.AutorisationImplementation;
import com.solidwall.tartib.services.AutorisationService;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;


@RestController
@RequestMapping("autorisation")
public class AutorisationController {

    @Autowired
    private AutorisationImplementation autorisationImplementation;

    @Autowired
    private AutorisationService autorisationService;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<AutorisationEntity>> create(@ModelAttribute CreateDto data) {
        CustomResponseHelper<AutorisationEntity> response = CustomResponseHelper.<AutorisationEntity>builder()
                .body(autorisationImplementation.create(data))
                .message("Autorisation created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<AutorisationEntity>> update(@PathVariable Long id,
            @ModelAttribute UpdateDto data) {
        CustomResponseHelper<AutorisationEntity> response = CustomResponseHelper.<AutorisationEntity>builder()
                .body(autorisationImplementation.update(id, data))
                .message("Autorisation updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<AutorisationEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<AutorisationEntity> response = CustomResponseHelper.<AutorisationEntity>builder()
                .body(autorisationImplementation.getOne(id))
                .message("Autorisation retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<AutorisationEntity>>> findAll() {
        CustomResponseHelper<List<AutorisationEntity>> response = CustomResponseHelper
                .<List<AutorisationEntity>>builder()
                .body(autorisationImplementation.findAll())
                .message("Autorisations list retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        autorisationImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Autorisation deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            Resource fileResource = autorisationService.downloadJustificationFile(id);

            AutorisationEntity autorisation = autorisationImplementation.getOne(id);

            String contentType = autorisationService.determineContentType(autorisation.getJustificationPath());

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        fileResource.getFilename() + "\"")
                .body(fileResource);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
