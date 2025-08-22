package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.linkedProject.CreateDto;
import com.solidwall.tartib.dto.linkedProject.UpdateDto;
import com.solidwall.tartib.entities.LinkedProjectEntity;

public interface LinkedProjectImplementation {
    List<LinkedProjectEntity> findAll();

    LinkedProjectEntity findOne();

    LinkedProjectEntity getOne(Long id);

    LinkedProjectEntity create(CreateDto data);

    LinkedProjectEntity update(Long id, UpdateDto data);

    void delete(Long id);

}
