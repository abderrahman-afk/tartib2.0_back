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
import com.solidwall.tartib.entities.MediaEntity;
import com.solidwall.tartib.implementations.MediaImplementation;

@RestController
@RequestMapping("media")
public class MediaController {

  @Autowired
  MediaImplementation mediaImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<MediaEntity>>> findAll() {
    CustomResponseHelper<List<MediaEntity>> response = CustomResponseHelper.<List<MediaEntity>>builder()
        .body(mediaImplementation.findAll())
        .message("media list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<MediaEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<MediaEntity> response = CustomResponseHelper.<MediaEntity>builder()
        .body(mediaImplementation.getOne(id))
        .message("media data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<MediaEntity>> create(@RequestBody MediaEntity data) {
    CustomResponseHelper<MediaEntity> response = CustomResponseHelper.<MediaEntity>builder()
        .body(mediaImplementation.create(data))
        .message("media created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<MediaEntity>> update(@PathVariable("id") Long id,
      @RequestBody MediaEntity user) {
    CustomResponseHelper<MediaEntity> response = CustomResponseHelper.<MediaEntity>builder()
        .body(mediaImplementation.update(id, user))
        .message("media updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    mediaImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("media deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
