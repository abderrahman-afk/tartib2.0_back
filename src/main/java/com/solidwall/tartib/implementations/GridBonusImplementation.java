package com.solidwall.tartib.implementations;

import java.util.List;
 
import com.solidwall.tartib.dto.gridbonus.CreateGridBonusDto;
 import com.solidwall.tartib.dto.gridbonus.GridBonusResponseDto;

public interface GridBonusImplementation {
    GridBonusResponseDto create(CreateGridBonusDto data);
    GridBonusResponseDto update(Long id, CreateGridBonusDto data);
    GridBonusResponseDto getOne(Long id);
    List<GridBonusResponseDto> findByClassification(Long classificationId);
    void delete(Long id);
}
