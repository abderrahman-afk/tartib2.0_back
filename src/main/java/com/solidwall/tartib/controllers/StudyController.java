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
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.study.*;
import com.solidwall.tartib.entities.StudyEntity;
import com.solidwall.tartib.implementations.StudyImplementation;

@RestController
@RequestMapping("study")
public class StudyController {

    @Autowired
    StudyImplementation studyImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StudyEntity>>> findAll() {
        CustomResponseHelper<List<StudyEntity>> response = CustomResponseHelper.<List<StudyEntity>>builder()
                .body(studyImplementation.findAll())
                .message("Studies list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<CustomResponseHelper<StudyEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StudyEntity> response = CustomResponseHelper.<StudyEntity>builder()
                .body(studyImplementation.getOne(id))
                .message("Study data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<CustomResponseHelper<StudyEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<StudyEntity> response = CustomResponseHelper.<StudyEntity>builder()
                .body(studyImplementation.create(data))
                .message("Study created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CustomResponseHelper<StudyEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<StudyEntity> response = CustomResponseHelper.<StudyEntity>builder()
                .body(studyImplementation.update(id, data))
                .message("Study updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        studyImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("Study deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
