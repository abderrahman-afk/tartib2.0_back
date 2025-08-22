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
import com.solidwall.tartib.entities.StrategyAxisEntity;
import com.solidwall.tartib.implementations.StrategyAxisImplementation;

@RestController
@RequestMapping("strategy-axis")
public class StrategyAxisController {

    @Autowired
    StrategyAxisImplementation strategyAxisImplementation;

    @GetMapping("all")
    public ResponseEntity<CustomResponseHelper<List<StrategyAxisEntity>>> findAll() {
        CustomResponseHelper<List<StrategyAxisEntity>> response = CustomResponseHelper.<List<StrategyAxisEntity>>builder()
                .body(strategyAxisImplementation.findAll())
                .message("strategy ax list")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping({ "get/{id}" })
    private ResponseEntity<CustomResponseHelper<StrategyAxisEntity>> getOne(@PathVariable Long id) {
        CustomResponseHelper<StrategyAxisEntity> response = CustomResponseHelper.<StrategyAxisEntity>builder()
                .body(strategyAxisImplementation.getOne(id))
                .message("strategy ax data")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({ "create" })
    private ResponseEntity<CustomResponseHelper<StrategyAxisEntity>> create(@RequestBody StrategyAxisEntity data) {
        CustomResponseHelper<StrategyAxisEntity> response = CustomResponseHelper.<StrategyAxisEntity>builder()
                .body(strategyAxisImplementation.create(data))
                .message("strategy ax created successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping({ "update/{id}" })
    private ResponseEntity<CustomResponseHelper<StrategyAxisEntity>> update(@PathVariable("id") Long id,
            @RequestBody StrategyAxisEntity user) {
        CustomResponseHelper<StrategyAxisEntity> response = CustomResponseHelper.<StrategyAxisEntity>builder()
                .body(strategyAxisImplementation.update(id, user))
                .message("strategy ax updated successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "delete/{id}" })
    private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
        strategyAxisImplementation.delete(id);
        CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
                .message("strategy ax deleted successfully")
                .error(false)
                .status(HttpStatus.OK.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
