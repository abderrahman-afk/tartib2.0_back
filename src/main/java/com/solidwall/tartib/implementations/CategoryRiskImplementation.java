package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.CategoryRiskEntity;

public interface CategoryRiskImplementation {

    List<CategoryRiskEntity> findAll();

    CategoryRiskEntity findOne();

    CategoryRiskEntity getOne(Long id);

    CategoryRiskEntity create(CategoryRiskEntity data);

    CategoryRiskEntity update(Long id, CategoryRiskEntity data);

    void delete(Long id);
}
