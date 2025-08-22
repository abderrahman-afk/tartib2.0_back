package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.activity.CreateDto;
import com.solidwall.tartib.dto.activity.UpdateDto;
import com.solidwall.tartib.entities.ActivityEntity;

public interface ActivityImplementation {

    List<ActivityEntity> findAll();

    ActivityEntity getOne(Long id);

    ActivityEntity findOne(Map<String, String> data);

    ActivityEntity create(CreateDto data);

    ActivityEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
