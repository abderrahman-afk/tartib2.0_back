package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.BudgetSectionEntity;

public interface BudgetSectionImplementation {

    List<BudgetSectionEntity> findAll();

    BudgetSectionEntity findOne();

    BudgetSectionEntity getOne(Long id);

    BudgetSectionEntity create(BudgetSectionEntity data);

    BudgetSectionEntity update(Long id, BudgetSectionEntity data);

    void delete(Long id);
}
