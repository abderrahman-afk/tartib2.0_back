package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.CurrencyEntity;
import com.solidwall.tartib.implementations.CurrencyImplementation;
import com.solidwall.tartib.repositories.CurrencyRepository;

@Service
public class CurrencyService implements CurrencyImplementation {

  @Autowired
  CurrencyRepository currencyRepository;

  @Override
  public List<CurrencyEntity> findAll() {
    if (!currencyRepository.findAll().isEmpty()) {
      return currencyRepository.findAll();
    } else {
      throw new NotFoundException("not exist any currencies");
    }
  }

  @Override
  public CurrencyEntity getOne(Long id) {
    Optional<CurrencyEntity> currency = currencyRepository.findById(id);
    if (currency.isPresent()) {
      return currency.get();
    } else {
      throw new NotFoundException("currency not exist");
    }
  }

  @Override
  public CurrencyEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public CurrencyEntity create(CurrencyEntity data) {
    Optional<CurrencyEntity> currency = currencyRepository.findByName(data.getName());
    if (!currency.isPresent()) {
      return currencyRepository.save(data);
    } else {
      throw new FoundException("currency already exist");
    }
  }

  @Override
  public CurrencyEntity update(Long id, CurrencyEntity data) {
    Optional<CurrencyEntity> currency = currencyRepository.findById(id);
    if (currency.isPresent()) {
      CurrencyEntity updateCurrency = currency.get();
      updateCurrency.setName(data.getName());
      updateCurrency.setCode(data.getCode());
      updateCurrency.setDescription(data.getDescription());
      updateCurrency.setActive(data.isActive());
      updateCurrency.setUpdatedAt(data.getUpdatedAt());
      return currencyRepository.save(updateCurrency);
    } else {
      throw new NotFoundException("currency not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<CurrencyEntity> data = currencyRepository.findById(id);
    if (data.isPresent()) {
      currencyRepository.deleteById(id);
    } else {
      throw new NotFoundException("currency not exist");
    }
  }
}
