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
import com.solidwall.tartib.dto.pndAxe.CreateDto;
import com.solidwall.tartib.dto.pndAxe.PndAxisResponseDto;
import com.solidwall.tartib.dto.pndAxe.UpdateDto;
// import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.entities.PndAxisEntity;
import com.solidwall.tartib.implementations.PndAxisImplementation;

@RestController
@RequestMapping("pnd-axis")
public class PndAxisController {

    @Autowired
    PndAxisImplementation pndAxisImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<PndAxisEntity>>> findAll() {
        CustomResponseHelper<List<PndAxisEntity>> response = CustomResponseHelper.<List<PndAxisEntity>>builder()
                .body(pndAxisImplementation.findAll())
                .message("pnd axis list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<PndAxisResponseDto>> getOne(@PathVariable Long id) {
        CustomResponseHelper<PndAxisResponseDto> response = CustomResponseHelper.<PndAxisResponseDto>builder()
                .body(pndAxisImplementation.getOne(id))
                .message("pnd axis data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<PndAxisEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<PndAxisEntity> response = CustomResponseHelper.<PndAxisEntity>builder()
                .body(pndAxisImplementation.create(data))
                .message("pnd axis created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<PndAxisEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<PndAxisEntity> response = CustomResponseHelper.<PndAxisEntity>builder()
                .body(pndAxisImplementation.update(id, data))
                .message("pnd axis updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        pndAxisImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("pnd axis deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping({"findByPnd/{id}"})
  public ResponseEntity<CustomResponseHelper<List<PndAxisEntity>>> findByDistrict(@PathVariable  Long id ) {
    CustomResponseHelper<List<PndAxisEntity>> response = CustomResponseHelper.<List<PndAxisEntity>>builder()
        .body(pndAxisImplementation.findByPnd(id))
        .message("pnd axis list by pnd")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
    
}
