package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.CategoryEnvergureEntity;

public interface CategoryEnvergureImplementation {

    List<CategoryEnvergureEntity> findAll();

    CategoryEnvergureEntity findOne();

    CategoryEnvergureEntity getOne(Long id);

    CategoryEnvergureEntity create(CategoryEnvergureEntity data);

    CategoryEnvergureEntity update(Long id, CategoryEnvergureEntity data);

    void delete(Long id);
}
