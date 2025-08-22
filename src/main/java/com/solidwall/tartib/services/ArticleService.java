package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.ArticleEntity;
import com.solidwall.tartib.implementations.ArticleImplementation;
import com.solidwall.tartib.repositories.ArticleRepository;

@Service
public class ArticleService implements ArticleImplementation {

  @Autowired
  ArticleRepository articleRepository;

  @Override
  public ArticleEntity getOne(Long id) {
    Optional<ArticleEntity> article = articleRepository.findById(id);
    if (article.isPresent()) {
      return article.get();
    } else {
      throw new NotFoundException("article not exist");
    }
  }

  @Override
  public ArticleEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public List<ArticleEntity> findAll() {
    if (!articleRepository.findAll().isEmpty()) {
      return articleRepository.findAll();
    } else {
      throw new NotFoundException("not exist any article ");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ArticleEntity> article = articleRepository.findById(id);
    if (article.isPresent()) {
      articleRepository.deleteById(id);
    } else {
      throw new NotFoundException("article not exist");
    }
  }

  @Override
  public ArticleEntity create(ArticleEntity data) {
    Optional<ArticleEntity> article = articleRepository.findByName(data.getName());
    if (!article.isPresent()) {
      return articleRepository.save(data);
    } else {
      throw new FoundException("article exist");
    }
  }

  @Override
  public ArticleEntity update(Long id, ArticleEntity data) {
    Optional<ArticleEntity> article = articleRepository.findById(id);
    if (article.isPresent()) {
      ArticleEntity updateArticle = article.get();
      updateArticle.setName(data.getName());
      updateArticle.setShort_description(data.getShort_description());
      updateArticle.setLong_description(data.getLong_description());
      updateArticle.setPicture(data.getPicture());
      updateArticle.setContent(data.getContent());
      updateArticle.setActive(data.isActive());
      updateArticle.setPrivate(data.isPrivate());
      updateArticle.setUpdatedAt(data.getUpdatedAt());
      return articleRepository.save(updateArticle);
    } else {
      throw new NotFoundException("article not found");
    }
  }
}
