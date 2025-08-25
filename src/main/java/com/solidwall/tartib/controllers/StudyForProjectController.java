package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.studyforproject.CreateDto;
import com.solidwall.tartib.dto.studyforproject.UpdateDto;
import com.solidwall.tartib.entities.StudyForProject;
import com.solidwall.tartib.implementations.StudyForProjectImplementation;
import com.solidwall.tartib.services.StudyForProjectService;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("study-for-project")
public class StudyForProjectController {

    @Autowired
    private StudyForProjectImplementation studyForProjectImplementation;

    @Autowired
    private StudyForProjectService studyForProjectService;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<StudyForProject>> create(@ModelAttribute CreateDto data) {
        CustomResponseHelper<StudyForProject> response = CustomResponseHelper.<StudyForProject>builder()
                .body(studyForProjectImplementation.create(data))
                .message("StudyForProject created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponseHelper<StudyForProject>> update(@PathVariable Long id,
            @ModelAttribute UpdateDto data) {
        CustomResponseHelper<StudyForProject> response = CustomResponseHelper.<StudyForProject>builder()
                .body(studyForProjectImplementation.update(id, data))
                .message("StudyForProject updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<StudyForProject>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StudyForProject> response = CustomResponseHelper.<StudyForProject>builder()
                .body(studyForProjectImplementation.getOne(id))
                .message("StudyForProject retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StudyForProject>>> findAll() {
        CustomResponseHelper<List<StudyForProject>> response = CustomResponseHelper
                .<List<StudyForProject>>builder()
                .body(studyForProjectImplementation.findAll())
                .message("StudyForProjects list retrieved successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        studyForProjectImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("StudyForProject deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            Resource fileResource = studyForProjectService.downloadStudyFile(id);

            StudyForProject studyForProject = studyForProjectImplementation.getOne(id);

            String contentType = studyForProjectService.determineContentType(studyForProject.getStudyFile());

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
