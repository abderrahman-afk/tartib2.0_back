package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.CategoryEntity;

public interface CategoryImplementation {

    List<CategoryEntity> findAll();

    CategoryEntity findOne();

    CategoryEntity getOne(Long id);

    CategoryEntity create(CategoryEntity data);

    CategoryEntity update(Long id, CategoryEntity data);

    void delete(Long id);
}
