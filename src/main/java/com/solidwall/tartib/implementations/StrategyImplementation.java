package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.StrategyEntity;

public interface StrategyImplementation {

    List<StrategyEntity> findAll();

    StrategyEntity findOne();

    StrategyEntity getOne(Long id);

    StrategyEntity create(StrategyEntity data);

    StrategyEntity update(Long id, StrategyEntity data);

    void delete(Long id);
}
