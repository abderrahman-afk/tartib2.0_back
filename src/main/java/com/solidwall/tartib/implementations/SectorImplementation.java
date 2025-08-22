package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.dto.sector.CreateDto;
import com.solidwall.tartib.dto.sector.UpdateDto;
import com.solidwall.tartib.entities.SectorEntity;

public interface SectorImplementation {

    List<SectorEntity> findAll();

    SectorEntity findOne();

    SectorEntity getOne(Long id);

    SectorEntity create(CreateDto data);

    SectorEntity update(Long id, UpdateDto data);

    void delete(Long id);
}
