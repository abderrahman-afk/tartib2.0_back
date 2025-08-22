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
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.implementations.MinisterImplementation;

@RestController
@RequestMapping("minister")
public class MinisterController {

    @Autowired
    MinisterImplementation ministerImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<MinisterEntity>>> findAll() {
        CustomResponseHelper<List<MinisterEntity>> response = CustomResponseHelper.<List<MinisterEntity>>builder()
                .body(ministerImplementation.findAll())
                .message("minister list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<MinisterEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<MinisterEntity> response = CustomResponseHelper.<MinisterEntity>builder()
                .body(ministerImplementation.getOne(id))
                .message("minister data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<MinisterEntity>> create(@RequestBody MinisterEntity data) {
        CustomResponseHelper<MinisterEntity> response = CustomResponseHelper.<MinisterEntity>builder()
                .body(ministerImplementation.create(data))
                .message("minister created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<MinisterEntity>> update(@PathVariable("id") Long id,
            @RequestBody MinisterEntity user) {
        CustomResponseHelper<MinisterEntity> response = CustomResponseHelper.<MinisterEntity>builder()
                .body(ministerImplementation.update(id, user))
                .message("minister updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        ministerImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("minister deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
