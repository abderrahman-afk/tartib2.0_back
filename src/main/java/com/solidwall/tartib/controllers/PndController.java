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
import com.solidwall.tartib.dto.pnd.PndDto;
import com.solidwall.tartib.entities.PndEntity;
import com.solidwall.tartib.implementations.PndImplementation;

@RestController
@RequestMapping("pnd")
public class PndController {

  @Autowired
  PndImplementation pndImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<PndDto>>> findAll() {
    CustomResponseHelper<List<PndDto>> response = CustomResponseHelper.<List<PndDto>>builder()
        .body(pndImplementation.findAll())
        .message("PND list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<PndDto>> getOne(@PathVariable Long id) {
    CustomResponseHelper<PndDto> response = CustomResponseHelper.<PndDto>builder()
        .body(pndImplementation.getOne(id))
        .message("PND data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<PndDto>> create(@RequestBody PndEntity data) {
    CustomResponseHelper<PndDto> response = CustomResponseHelper.<PndDto>builder()
        .body(pndImplementation.create(data))
        .message("PND created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<PndDto>> update(@PathVariable("id") Long id,
      @RequestBody PndEntity user) {
    CustomResponseHelper<PndDto> response = CustomResponseHelper.<PndDto>builder()
        .body(pndImplementation.update(id, user))
        .message("PND updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    pndImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("PND deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
