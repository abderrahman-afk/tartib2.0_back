package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.MinisterEntity;

public interface MinisterImplementation {

    List<MinisterEntity> findAll();

    MinisterEntity findOne();

    MinisterEntity getOne(Long id);

    MinisterEntity create(MinisterEntity data);

    MinisterEntity update(Long id, MinisterEntity data);

    void delete(Long id);
}
