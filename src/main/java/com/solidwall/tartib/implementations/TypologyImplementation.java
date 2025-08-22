package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.TypologyEntity;

public interface TypologyImplementation {

    List<TypologyEntity> findAll();

    TypologyEntity findOne();

    TypologyEntity getOne(Long id);

    TypologyEntity create(TypologyEntity data);

    TypologyEntity update(Long id, TypologyEntity data);

    void delete(Long id);
}
