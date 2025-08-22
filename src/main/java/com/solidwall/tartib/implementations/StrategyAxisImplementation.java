package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.StrategyAxisEntity;

public interface StrategyAxisImplementation {

    List<StrategyAxisEntity> findAll();

    StrategyAxisEntity findOne();

    StrategyAxisEntity getOne(Long id);

    StrategyAxisEntity create(StrategyAxisEntity data);

    StrategyAxisEntity update(Long id, StrategyAxisEntity data);

    void delete(Long id);
}
