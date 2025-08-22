package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.EconomicNatureEntity;

public interface EconomicNatureImplementation {

    List<EconomicNatureEntity> findAll();

    EconomicNatureEntity findOne();

    EconomicNatureEntity getOne(Long id);

    EconomicNatureEntity create(EconomicNatureEntity data);

    EconomicNatureEntity update(Long id, EconomicNatureEntity data);

    void delete(Long id);
}
