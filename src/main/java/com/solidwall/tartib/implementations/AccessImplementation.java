package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.access.*;
import com.solidwall.tartib.entities.AccessEntity;

public interface AccessImplementation {

    List<AccessEntity> findAll();

    AccessEntity findOne(Map<String, String> data);

    AccessEntity getOne(Long id);

    AccessEntity create(CreateDto data);

    AccessEntity update(Long id, UpdateDto data);

    void delete(Long id);

}
