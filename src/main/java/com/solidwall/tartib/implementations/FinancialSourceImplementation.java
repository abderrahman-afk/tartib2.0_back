package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.financialsource.*;
import com.solidwall.tartib.entities.FinancialSourceEntity;

public interface FinancialSourceImplementation {
        List<FinancialSourceEntity> findAll();

    FinancialSourceEntity findOne();

    FinancialSourceEntity getOne(Long id);

    FinancialSourceEntity create(CreateDto data);

    FinancialSourceEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
