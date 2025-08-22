package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.nomenclatureGeographic.CreateNomenclatureGeographicDto;
import com.solidwall.tartib.dto.nomenclatureGeographic.UpdateNomenclatureGeographicDto;
import com.solidwall.tartib.entities.NomenclatureGeographicEntity;

public interface NomenclatureGeographicImplementation {
    
    NomenclatureGeographicEntity create(CreateNomenclatureGeographicDto dto);

    NomenclatureGeographicEntity update(Long id, UpdateNomenclatureGeographicDto data);

    NomenclatureGeographicEntity getOne(Long id);

    List<NomenclatureGeographicEntity> findAll(Map<String, String> filters);

    void activate(Long id);

    void delete(Long id);

    void deactivateAll();

    NomenclatureGeographicEntity findActive();

}
