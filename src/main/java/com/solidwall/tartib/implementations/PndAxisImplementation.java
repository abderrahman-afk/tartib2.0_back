package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.pndAxe.CreateDto;
import com.solidwall.tartib.dto.pndAxe.PndAxisResponseDto;
import com.solidwall.tartib.dto.pndAxe.UpdateDto;
import com.solidwall.tartib.entities.PndAxisEntity;

public interface PndAxisImplementation {

    List<PndAxisEntity> findAll();
    List<PndAxisEntity> findByPnd(Long id);

    PndAxisEntity findOne();

    PndAxisResponseDto getOne(Long id);

    PndAxisEntity create(CreateDto data);

    PndAxisEntity update(Long id, UpdateDto data);

    void delete(Long id);
}