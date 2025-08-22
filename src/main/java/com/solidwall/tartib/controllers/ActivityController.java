package com.solidwall.tartib.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solidwall.tartib.core.helpers.CustomResponseHelper;
import com.solidwall.tartib.dto.activity.CreateDto;
import com.solidwall.tartib.dto.activity.UpdateDto;
import com.solidwall.tartib.entities.ActivityEntity;
import com.solidwall.tartib.implementations.ActivityImplementation;

@RestController
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    ActivityImplementation activityImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<ActivityEntity>>> findAll() {
        CustomResponseHelper<List<ActivityEntity>> response = CustomResponseHelper.<List<ActivityEntity>>builder()
                .body(activityImplementation.findAll())
                .message("activity list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<ActivityEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<ActivityEntity> response = CustomResponseHelper.<ActivityEntity>builder()
                .body(activityImplementation.getOne(id))
                .message("activity data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "find" })
    public ResponseEntity<CustomResponseHelper<ActivityEntity>> findOne(@RequestParam Map<String, String> reqParam) {
        CustomResponseHelper<ActivityEntity> response = CustomResponseHelper
                .<ActivityEntity>builder()
                .body(activityImplementation.findOne(reqParam))
                .message("activity found")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<ActivityEntity>> create(@RequestBody CreateDto data) {
        CustomResponseHelper<ActivityEntity> response = CustomResponseHelper.<ActivityEntity>builder()
                .body(activityImplementation.create(data))
                .message("activity created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<ActivityEntity>> update(@PathVariable("id") Long id,
            @RequestBody UpdateDto data) {
        CustomResponseHelper<ActivityEntity> response = CustomResponseHelper.<ActivityEntity>builder()
                .body(activityImplementation.update(id, data))
                .message("activity updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        activityImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("activity deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
