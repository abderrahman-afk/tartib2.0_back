package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.study.CreateDto;
import com.solidwall.tartib.dto.study.UpdateDto;

import com.solidwall.tartib.entities.StudyEntity;

public interface StudyImplementation {
    List<StudyEntity> findAll();

    StudyEntity getOne(Long id);

    StudyEntity create(CreateDto data);

    StudyEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
