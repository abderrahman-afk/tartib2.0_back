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
import com.solidwall.tartib.entities.CurrencyEntity;
import com.solidwall.tartib.implementations.CurrencyImplementation;
@RestController
@RequestMapping("currency")
public class CurrencyController {

    @Autowired
    CurrencyImplementation currencyImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<CurrencyEntity>>> findAll() {
        CustomResponseHelper<List<CurrencyEntity>> response = CustomResponseHelper.<List<CurrencyEntity>>builder()
                .body(currencyImplementation.findAll())
                .message("currency list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<CurrencyEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<CurrencyEntity> response = CustomResponseHelper.<CurrencyEntity>builder()
                .body(currencyImplementation.getOne(id))
                .message("currency data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<CurrencyEntity>> create(@RequestBody CurrencyEntity data) {
        CustomResponseHelper<CurrencyEntity> response = CustomResponseHelper.<CurrencyEntity>builder()
                .body(currencyImplementation.create(data))
                .message("currency created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<CurrencyEntity>> update(@PathVariable("id") Long id,
            @RequestBody CurrencyEntity user) {
        CustomResponseHelper<CurrencyEntity> response = CustomResponseHelper.<CurrencyEntity>builder()
                .body(currencyImplementation.update(id, user))
                .message("currency updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        currencyImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("currency deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
