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
import org.springframework.web.bind.annotation.RestController;
import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.entities.StudyStateEntity;
import com.solidwall.tartib.implementations.StudyStateImplementation;

@RestController
@RequestMapping("study-state")
public class StudyStateController {

    @Autowired
    StudyStateImplementation stateImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StudyStateEntity>>> findAll() {
        CustomResponseHelper<List<StudyStateEntity>> response = CustomResponseHelper.<List<StudyStateEntity>>builder()
                .body(stateImplementation.findAll())
                .message("study state list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<StudyStateEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StudyStateEntity> response = CustomResponseHelper.<StudyStateEntity>builder()
                .body(stateImplementation.getOne(id))
                .message("study state data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<StudyStateEntity>> create(@RequestBody StudyStateEntity data) {
        CustomResponseHelper<StudyStateEntity> response = CustomResponseHelper.<StudyStateEntity>builder()
                .body(stateImplementation.create(data))
                .message("study state created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<StudyStateEntity>> update(@PathVariable("id") Long id,
            @RequestBody StudyStateEntity user) {
        CustomResponseHelper<StudyStateEntity> response = CustomResponseHelper.<StudyStateEntity>builder()
                .body(stateImplementation.update(id, user))
                .message("study state updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        stateImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("study state deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
