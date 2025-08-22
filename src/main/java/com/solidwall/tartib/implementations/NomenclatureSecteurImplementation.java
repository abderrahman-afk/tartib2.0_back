package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.solidwall.tartib.dto.nomenclatureSecteur.CreateNomenclatureSecteurDto;
import com.solidwall.tartib.dto.nomenclatureSecteur.UpdateNomenclatureSecteurDto;
import com.solidwall.tartib.entities.NomenclatureSecteurEntity;

public interface NomenclatureSecteurImplementation {
    
    NomenclatureSecteurEntity create(CreateNomenclatureSecteurDto dto);

    NomenclatureSecteurEntity update(Long id, UpdateNomenclatureSecteurDto data);

    NomenclatureSecteurEntity getOne(Long id);

    List<NomenclatureSecteurEntity> findAll(Map<String, String> filters);

    void activate(Long id);

    void delete(Long id);

    void deactivateAll();

    NomenclatureSecteurEntity findActive();
 Resource downloadJustificationFile(Long id);
 String determineContentType(String filename);
}
