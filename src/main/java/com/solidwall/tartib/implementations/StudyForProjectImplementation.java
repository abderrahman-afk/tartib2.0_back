package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;

import com.solidwall.tartib.dto.studyforproject.CreateDto;
import com.solidwall.tartib.dto.studyforproject.UpdateDto;
import com.solidwall.tartib.entities.StudyForProject;

public interface StudyForProjectImplementation {
    List<StudyForProject> findAll();

    StudyForProject getOne(Long id);

    StudyForProject findOne(Map<String, String> data);

    StudyForProject create(CreateDto data);

    StudyForProject update(Long id, UpdateDto data);

    void delete(Long id);
}
