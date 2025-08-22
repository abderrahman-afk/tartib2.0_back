package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.dto.pnd.PndDto;
import com.solidwall.tartib.entities.PndEntity;

public interface PndImplementation {

    List<PndDto > findAll();

    PndDto  findOne();

    PndDto  getOne(Long id);

    PndDto  create(PndEntity data);
 
    PndDto  update(Long id, PndEntity data);

    void delete(Long id);
}
