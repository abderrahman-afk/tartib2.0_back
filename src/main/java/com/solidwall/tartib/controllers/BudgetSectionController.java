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
import com.solidwall.tartib.entities.BudgetSectionEntity;
import com.solidwall.tartib.implementations.BudgetSectionImplementation;

@RestController
@RequestMapping("budget")
public class BudgetSectionController {

    @Autowired
    BudgetSectionImplementation budgetSectionImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<BudgetSectionEntity>>> findAll() {
        CustomResponseHelper<List<BudgetSectionEntity>> response = CustomResponseHelper
                .<List<BudgetSectionEntity>>builder()
                .body(budgetSectionImplementation.findAll())
                .message("budget section list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<BudgetSectionEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<BudgetSectionEntity> response = CustomResponseHelper.<BudgetSectionEntity>builder()
                .body(budgetSectionImplementation.getOne(id))
                .message("budget section data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<BudgetSectionEntity>> create(@RequestBody BudgetSectionEntity data) {
        CustomResponseHelper<BudgetSectionEntity> response = CustomResponseHelper.<BudgetSectionEntity>builder()
                .body(budgetSectionImplementation.create(data))
                .message("budget section created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<BudgetSectionEntity>> update(@PathVariable("id") Long id,
            @RequestBody BudgetSectionEntity user) {
        CustomResponseHelper<BudgetSectionEntity> response = CustomResponseHelper.<BudgetSectionEntity>builder()
                .body(budgetSectionImplementation.update(id, user))
                .message("budget section updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        budgetSectionImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("budget section deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
