package com.solidwall.tartib.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.study.CreateDto;
import com.solidwall.tartib.dto.study.UpdateDto;
import com.solidwall.tartib.entities.StudyEntity;
import com.solidwall.tartib.implementations.StudyImplementation;
import com.solidwall.tartib.repositories.StudyRepository;

@Service
public class StudyService implements StudyImplementation {
  @Autowired
    private StudyRepository studyRepository;

    @Override
    public List<StudyEntity> findAll() {
        List<StudyEntity> studies = studyRepository.findAll();
        if (!studies.isEmpty()) {
            return studies;
        } else {
            throw new NotFoundException("No studies found");
        }
    }

    @Override
    public StudyEntity getOne(Long id) {
        return studyRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Study not found"));
    }

    @Override
    public StudyEntity create(CreateDto data) {
        StudyEntity study = new StudyEntity();
        study.setName(data.getName());
        study.setDescription(data.getDescription());
        study.setRealisationDate(data.getRealisationDate());
        study.setActive(data.isActive());
        return studyRepository.save(study);
    }

    @Override
    public StudyEntity update(Long id, UpdateDto data) {
        StudyEntity study = studyRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Study not found"));

            study.setName(data.getName());
            study.setDescription(data.getDescription());
            study.setRealisationDate(data.getRealisationDate());
            study.setActive(data.isActive());

        return studyRepository.save(study);
    }

    @Override
    public void delete(Long id) {
        StudyEntity study = studyRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Study not found"));
        studyRepository.delete(study);
    }
}
