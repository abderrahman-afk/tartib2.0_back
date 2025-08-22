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
import com.solidwall.tartib.entities.FundingSourceTypeEntity;
import com.solidwall.tartib.implementations.FundingSourceTypeImplementation;

@RestController
@RequestMapping("funding-source-type")
public class FundingSourceTypeController {

    @Autowired
    FundingSourceTypeImplementation fundingSourceTypeImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<FundingSourceTypeEntity>>> findAll() {
        CustomResponseHelper<List<FundingSourceTypeEntity>> response = CustomResponseHelper
                .<List<FundingSourceTypeEntity>>builder()
                .body(fundingSourceTypeImplementation.findAll())
                .message("funding source type list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<FundingSourceTypeEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<FundingSourceTypeEntity> response = CustomResponseHelper.<FundingSourceTypeEntity>builder()
                .body(fundingSourceTypeImplementation.getOne(id))
                .message("funding source type data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<FundingSourceTypeEntity>> create(
            @RequestBody FundingSourceTypeEntity data) {
        CustomResponseHelper<FundingSourceTypeEntity> response = CustomResponseHelper.<FundingSourceTypeEntity>builder()
                .body(fundingSourceTypeImplementation.create(data))
                .message("funding source type created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<FundingSourceTypeEntity>> update(@PathVariable("id") Long id,
            @RequestBody FundingSourceTypeEntity data) {
        CustomResponseHelper<FundingSourceTypeEntity> response = CustomResponseHelper.<FundingSourceTypeEntity>builder()
                .body(fundingSourceTypeImplementation.update(id, data))
                .message("funding source type updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        fundingSourceTypeImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("funding source type deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
