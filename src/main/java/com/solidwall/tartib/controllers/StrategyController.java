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
import com.solidwall.tartib.entities.StrategyEntity;
import com.solidwall.tartib.implementations.StrategyImplementation;

@RestController
@RequestMapping("strategy")
public class StrategyController {

    @Autowired
    StrategyImplementation strategyImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StrategyEntity>>> findAll() {
        CustomResponseHelper<List<StrategyEntity>> response = CustomResponseHelper.<List<StrategyEntity>>builder()
                .body(strategyImplementation.findAll())
                .message("strategy list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<StrategyEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StrategyEntity> response = CustomResponseHelper.<StrategyEntity>builder()
                .body(strategyImplementation.getOne(id))
                .message("strategy data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<StrategyEntity>> create(@RequestBody StrategyEntity data) {
        CustomResponseHelper<StrategyEntity> response = CustomResponseHelper.<StrategyEntity>builder()
                .body(strategyImplementation.create(data))
                .message("strategy created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<StrategyEntity>> update(@PathVariable("id") Long id,
            @RequestBody StrategyEntity user) {
        CustomResponseHelper<StrategyEntity> response = CustomResponseHelper.<StrategyEntity>builder()
                .body(strategyImplementation.update(id, user))
                .message("strategy updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        strategyImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("strategy deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
