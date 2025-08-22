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
import com.solidwall.tartib.entities.TypologyEntity;
import com.solidwall.tartib.implementations.TypologyImplementation;

@RestController
@RequestMapping("typology")
public class TypologyController {

    @Autowired
    TypologyImplementation typologyImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<TypologyEntity>>> findAll() {
        CustomResponseHelper<List<TypologyEntity>> response = CustomResponseHelper
                .<List<TypologyEntity>>builder()
                .body(typologyImplementation.findAll())
                .message("typology list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<TypologyEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<TypologyEntity> response = CustomResponseHelper.<TypologyEntity>builder()
                .body(typologyImplementation.getOne(id))
                .message("typology data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<TypologyEntity>> create(
            @RequestBody TypologyEntity data) {
        CustomResponseHelper<TypologyEntity> response = CustomResponseHelper.<TypologyEntity>builder()
                .body(typologyImplementation.create(data))
                .message("typology created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<TypologyEntity>> update(@PathVariable("id") Long id,
            @RequestBody TypologyEntity data) {
        CustomResponseHelper<TypologyEntity> response = CustomResponseHelper.<TypologyEntity>builder()
                .body(typologyImplementation.update(id, data))
                .message("typology updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        typologyImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("typology deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
