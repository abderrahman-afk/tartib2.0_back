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
import com.solidwall.tartib.entities.CategoryEntity;
import com.solidwall.tartib.implementations.CategoryImplementation;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryImplementation categoryImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<CategoryEntity>>> findAll() {
        CustomResponseHelper<List<CategoryEntity>> response = CustomResponseHelper.<List<CategoryEntity>>builder()
                .body(categoryImplementation.findAll())
                .message("category list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<CategoryEntity> response = CustomResponseHelper.<CategoryEntity>builder()
                .body(categoryImplementation.getOne(id))
                .message("category data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<CategoryEntity>> create(@RequestBody CategoryEntity data) {
        CustomResponseHelper<CategoryEntity> response = CustomResponseHelper.<CategoryEntity>builder()
                .body(categoryImplementation.create(data))
                .message("category created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<CategoryEntity>> update(@PathVariable("id") Long id,
            @RequestBody CategoryEntity user) {
        CustomResponseHelper<CategoryEntity> response = CustomResponseHelper.<CategoryEntity>builder()
                .body(categoryImplementation.update(id, user))
                .message("category updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        categoryImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("category deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
