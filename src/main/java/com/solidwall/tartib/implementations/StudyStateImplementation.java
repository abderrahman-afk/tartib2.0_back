package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.StudyStateEntity;

public interface StudyStateImplementation {

    List<StudyStateEntity> findAll();

    StudyStateEntity findOne();

    StudyStateEntity getOne(Long id);

    StudyStateEntity create(StudyStateEntity data);

    StudyStateEntity update(Long id, StudyStateEntity data);

    void delete(Long id);
}
