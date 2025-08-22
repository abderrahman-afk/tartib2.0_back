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
import com.solidwall.tartib.entities.CategoryRiskEntity;
import com.solidwall.tartib.implementations.CategoryRiskImplementation;

@RestController
@RequestMapping("category-risk")
public class CategoryRiskController {

    @Autowired
    CategoryRiskImplementation categoryriskImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<CategoryRiskEntity>>> findAll() {
        CustomResponseHelper<List<CategoryRiskEntity>> response = CustomResponseHelper.<List<CategoryRiskEntity>>builder()
                .body(categoryriskImplementation.findAll())
                .message("category risk list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryRiskEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<CategoryRiskEntity> response = CustomResponseHelper.<CategoryRiskEntity>builder()
                .body(categoryriskImplementation.getOne(id))
                .message("category risk data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<CategoryRiskEntity>> create(@RequestBody CategoryRiskEntity data) {
        CustomResponseHelper<CategoryRiskEntity> response = CustomResponseHelper.<CategoryRiskEntity>builder()
                .body(categoryriskImplementation.create(data))
                .message("category risk created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryRiskEntity>> update(@PathVariable("id") Long id,
            @RequestBody CategoryRiskEntity user) {
        CustomResponseHelper<CategoryRiskEntity> response = CustomResponseHelper.<CategoryRiskEntity>builder()
                .body(categoryriskImplementation.update(id, user))
                .message("category risk updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        categoryriskImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("category risk deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
