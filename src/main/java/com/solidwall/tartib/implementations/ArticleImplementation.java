package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.ArticleEntity;

public interface ArticleImplementation {

  List<ArticleEntity> findAll();

  ArticleEntity findOne();

  ArticleEntity getOne(Long id);

  ArticleEntity create(ArticleEntity data);

  ArticleEntity update(Long id, ArticleEntity data);

  void delete(Long id);

}
