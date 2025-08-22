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
import com.solidwall.tartib.entities.CategoryEnvergureEntity;
import com.solidwall.tartib.implementations.CategoryEnvergureImplementation;

@RestController
@RequestMapping("category-envergure")
public class CategoryEnvergureController {


    @Autowired
    CategoryEnvergureImplementation categoryEnvergureImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<CategoryEnvergureEntity>>> findAll() {
        CustomResponseHelper<List<CategoryEnvergureEntity>> response = CustomResponseHelper
                .<List<CategoryEnvergureEntity>>builder()
                .body(categoryEnvergureImplementation.findAll())
                .message("envergure list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryEnvergureEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<CategoryEnvergureEntity> response = CustomResponseHelper.<CategoryEnvergureEntity>builder()
                .body(categoryEnvergureImplementation.getOne(id))
                .message("envergure data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<CategoryEnvergureEntity>> create(
            @RequestBody CategoryEnvergureEntity data) {
        CustomResponseHelper<CategoryEnvergureEntity> response = CustomResponseHelper.<CategoryEnvergureEntity>builder()
                .body(categoryEnvergureImplementation.create(data))
                .message("envergure created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryEnvergureEntity>> update(@PathVariable("id") Long id,
            @RequestBody CategoryEnvergureEntity data) {
        CustomResponseHelper<CategoryEnvergureEntity> response = CustomResponseHelper.<CategoryEnvergureEntity>builder()
                .body(categoryEnvergureImplementation.update(id, data))
                .message("envergure updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        categoryEnvergureImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("envergure deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
