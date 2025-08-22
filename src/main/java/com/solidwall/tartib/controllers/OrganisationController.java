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
import com.solidwall.tartib.entities.OrganisationEntity;
import com.solidwall.tartib.implementations.OrganisationImplementation;

@RestController
@RequestMapping("organisation")
public class OrganisationController {

    @Autowired
    OrganisationImplementation organisationImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<OrganisationEntity>>> findAll() {
        CustomResponseHelper<List<OrganisationEntity>> response = CustomResponseHelper.<List<OrganisationEntity>>builder()
                .body(organisationImplementation.findAll())
                .message("organism list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<OrganisationEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<OrganisationEntity> response = CustomResponseHelper.<OrganisationEntity>builder()
                .body(organisationImplementation.getOne(id))
                .message("organism data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<OrganisationEntity>> create(@RequestBody OrganisationEntity data) {
        CustomResponseHelper<OrganisationEntity> response = CustomResponseHelper.<OrganisationEntity>builder()
                .body(organisationImplementation.create(data))
                .message("organism created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<OrganisationEntity>> update(@PathVariable("id") Long id,
            @RequestBody OrganisationEntity user) {
        CustomResponseHelper<OrganisationEntity> response = CustomResponseHelper.<OrganisationEntity>builder()
                .body(organisationImplementation.update(id, user))
                .message("organism updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        organisationImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("organism deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
