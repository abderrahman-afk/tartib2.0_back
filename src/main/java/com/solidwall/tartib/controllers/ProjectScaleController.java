package com.solidwall.tartib.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.projectscale.CreateDto;
import com.solidwall.tartib.dto.projectscale.UpdateDto;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import com.solidwall.tartib.implementations.ProjectScaleImplementation;

@RestController
@RequestMapping("project-scale")
public class ProjectScaleController {

    @Autowired
    ProjectScaleImplementation projectScaleImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<ProjectScaleEntity>>> findAll() {
        CustomResponseHelper<List<ProjectScaleEntity>> response = CustomResponseHelper
                .<List<ProjectScaleEntity>>builder()
                .body(projectScaleImplementation.findAll())
                .message("Project scales list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<ProjectScaleEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<ProjectScaleEntity> response = CustomResponseHelper.<ProjectScaleEntity>builder()
                .body(projectScaleImplementation.getOne(id))
                .message("Project scale data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("getByIdentity/{id}")
    public ResponseEntity<CustomResponseHelper<List<ProjectScaleEntity>>> getOneByIdentity(@PathVariable Long id) {
        CustomResponseHelper<List<ProjectScaleEntity>> response = CustomResponseHelper.<List<ProjectScaleEntity>>builder()
                .body(projectScaleImplementation.findByProjectIdentity(id))
                .message("Project scale data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    
    @GetMapping("getBythreshhole")
    public ResponseEntity<CustomResponseHelper<List<ProjectScaleEntity>>> getOneByThreshhold(@RequestParam Long minimum,@RequestParam Long maximum) {
        CustomResponseHelper<List<ProjectScaleEntity>> response = CustomResponseHelper.<List<ProjectScaleEntity>>builder()
                .body(projectScaleImplementation.findByMaximumBudgetAndMinimumBudget(minimum,maximum))
                .message("Project scale data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<ProjectScaleEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<ProjectScaleEntity> response = CustomResponseHelper.<ProjectScaleEntity>builder()
                .body(projectScaleImplementation.create(data))
                .message("Project scale created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<ProjectScaleEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<ProjectScaleEntity> response = CustomResponseHelper.<ProjectScaleEntity>builder()
                .body(projectScaleImplementation.update(id, data))
                .message("Project scale updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        projectScaleImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Project scale deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("add-study/{id}")
    public ResponseEntity<CustomResponseHelper<ProjectScaleEntity>> addStudyToProjectScale(@PathVariable Long id,
            @RequestBody Long studies) {
        CustomResponseHelper<ProjectScaleEntity> response = CustomResponseHelper.<ProjectScaleEntity>builder()
                .body(projectScaleImplementation.addStudyToProjectScale(id, studies))
                .message("Study added to project scale")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/add-studies/{projectId}")
    public ResponseEntity<CustomResponseHelper<Void>> addStudiesToProjectScale(
            @PathVariable Long projectId,
            @RequestBody List<Long> studyIds) {

        projectScaleImplementation.addStudiesToProjectScale(projectId, studyIds);

        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Studies added to project scale successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}