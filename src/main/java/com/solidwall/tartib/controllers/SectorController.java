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
import com.solidwall.tartib.dto.sector.CreateDto;
import com.solidwall.tartib.dto.sector.UpdateDto;
import com.solidwall.tartib.entities.SectorEntity;
import com.solidwall.tartib.implementations.SectorImplementation;

@RestController
@RequestMapping("sector")
public class SectorController {

    @Autowired
    SectorImplementation sectorImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<SectorEntity>>> findAll() {
        CustomResponseHelper<List<SectorEntity>> response = CustomResponseHelper.<List<SectorEntity>>builder()
                .body(sectorImplementation.findAll())
                .message("sector list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<SectorEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<SectorEntity> response = CustomResponseHelper.<SectorEntity>builder()
                .body(sectorImplementation.getOne(id))
                .message("sector data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<SectorEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<SectorEntity> response = CustomResponseHelper.<SectorEntity>builder()
                .body(sectorImplementation.create(data))
                .message("sector created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<SectorEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto user) {
        CustomResponseHelper<SectorEntity> response = CustomResponseHelper.<SectorEntity>builder()
                .body(sectorImplementation.update(id, user))
                .message("sector updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        sectorImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("sector deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);

    }
}
