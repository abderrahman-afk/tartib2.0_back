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
import com.solidwall.tartib.entities.ArticleEntity;
import com.solidwall.tartib.implementations.ArticleImplementation;

@RestController
@RequestMapping("article")
public class ArticleController {

  @Autowired
  ArticleImplementation articleImplementation;

  @GetMapping("all")
  public ResponseEntity<CustomResponseHelper<List<ArticleEntity>>> findAll() {
    CustomResponseHelper<List<ArticleEntity>> response = CustomResponseHelper.<List<ArticleEntity>>builder()
        .body(articleImplementation.findAll())
        .message("articles list")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping({ "get/{id}" })
  private ResponseEntity<CustomResponseHelper<ArticleEntity>> getOne(@PathVariable Long id) {
    CustomResponseHelper<ArticleEntity> response = CustomResponseHelper.<ArticleEntity>builder()
        .body(articleImplementation.getOne(id))
        .message("article data")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping({ "create" })
  private ResponseEntity<CustomResponseHelper<ArticleEntity>> create(@RequestBody ArticleEntity data) {
    CustomResponseHelper<ArticleEntity> response = CustomResponseHelper.<ArticleEntity>builder()
        .body(articleImplementation.create(data))
        .message("article created successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping({ "update/{id}" })
  private ResponseEntity<CustomResponseHelper<ArticleEntity>> update(@PathVariable("id") Long id,
      @RequestBody ArticleEntity user) {
    CustomResponseHelper<ArticleEntity> response = CustomResponseHelper.<ArticleEntity>builder()
        .body(articleImplementation.update(id, user))
        .message("article updated successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping({ "delete/{id}" })
  private ResponseEntity<CustomResponseHelper<Void>> delete(@PathVariable Long id) {
    articleImplementation.delete(id);
    CustomResponseHelper<Void> response = CustomResponseHelper.<Void>builder()
        .message("article deleted successfully")
        .error(false)
        .status(HttpStatus.OK.value())
        .timestamp(new Date())
        .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
