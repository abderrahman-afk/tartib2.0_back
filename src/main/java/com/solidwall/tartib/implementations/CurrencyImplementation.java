package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.CurrencyEntity;

public interface CurrencyImplementation {

    List<CurrencyEntity> findAll();

    CurrencyEntity findOne();

    CurrencyEntity getOne(Long id);

    CurrencyEntity create(CurrencyEntity data);

    CurrencyEntity update(Long id, CurrencyEntity data);

    void delete(Long id);
}
