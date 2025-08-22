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
import com.solidwall.tartib.entities.EconomicNatureEntity;
import com.solidwall.tartib.implementations.EconomicNatureImplementation;

@RestController
@RequestMapping("economic-nature")
public class EconomicNatureController {

    @Autowired
    EconomicNatureImplementation economicNatureImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<EconomicNatureEntity>>> findAll() {
        CustomResponseHelper<List<EconomicNatureEntity>> response = CustomResponseHelper
                .<List<EconomicNatureEntity>>builder()
                .body(economicNatureImplementation.findAll())
                .message("economic list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<EconomicNatureEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<EconomicNatureEntity> response = CustomResponseHelper.<EconomicNatureEntity>builder()
                .body(economicNatureImplementation.getOne(id))
                .message("economic nature data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<EconomicNatureEntity>> create(@RequestBody EconomicNatureEntity data) {
        CustomResponseHelper<EconomicNatureEntity> response = CustomResponseHelper.<EconomicNatureEntity>builder()
                .body(economicNatureImplementation.create(data))
                .message("economic nature created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<EconomicNatureEntity>> update(@PathVariable("id") Long id,
            @RequestBody EconomicNatureEntity user) {
        CustomResponseHelper<EconomicNatureEntity> response = CustomResponseHelper.<EconomicNatureEntity>builder()
                .body(economicNatureImplementation.update(id, user))
                .message("economic nature updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        economicNatureImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("economic nature deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
