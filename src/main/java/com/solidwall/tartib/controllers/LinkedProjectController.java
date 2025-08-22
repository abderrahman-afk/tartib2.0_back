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
import com.solidwall.tartib.dto.linkedProject.CreateDto;
import com.solidwall.tartib.dto.linkedProject.UpdateDto;
import com.solidwall.tartib.entities.LinkedProjectEntity;
import com.solidwall.tartib.implementations.LinkedProjectImplementation;

@RestController
@RequestMapping("LinkedProject")
public class LinkedProjectController {
    
    @Autowired
    LinkedProjectImplementation LinkedProjectImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<LinkedProjectEntity>>> findAll() {
        CustomResponseHelper<List<LinkedProjectEntity>> response = CustomResponseHelper.<List<LinkedProjectEntity>>builder()
                .body(LinkedProjectImplementation.findAll())
                .message("LinkedProject list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<LinkedProjectEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<LinkedProjectEntity> response = CustomResponseHelper.<LinkedProjectEntity>builder()
                .body(LinkedProjectImplementation.getOne(id))
                .message("LinkedProject data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<LinkedProjectEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<LinkedProjectEntity> response = CustomResponseHelper.<LinkedProjectEntity>builder()
                .body(LinkedProjectImplementation.create(data))
                .message("LinkedProject created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<LinkedProjectEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<LinkedProjectEntity> response = CustomResponseHelper.<LinkedProjectEntity>builder()
                .body(LinkedProjectImplementation.update(id, data))
                .message("LinkedProject updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        LinkedProjectImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("LinkedProject deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
