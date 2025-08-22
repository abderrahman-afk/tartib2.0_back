package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.project.zone.CreateDto;
import com.solidwall.tartib.dto.project.zone.UpdateDto;
import com.solidwall.tartib.entities.ProjectZoneEntity;

public interface ProjectZoneImplementation {

    List<ProjectZoneEntity> findAll();

    ProjectZoneEntity findOne(Map<String, String> data);

    ProjectZoneEntity getOne(Long id);

    ProjectZoneEntity create(CreateDto data);

    ProjectZoneEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
