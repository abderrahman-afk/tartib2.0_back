package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.StudyStateEntity;
import com.solidwall.tartib.implementations.StudyStateImplementation;
import com.solidwall.tartib.repositories.StudyStateRepository;

@Service
public class StudyStateService implements StudyStateImplementation {

    @Autowired
    StudyStateRepository studyStateRepository;

    @Override
    public StudyStateEntity getOne(Long id) {
        Optional<StudyStateEntity> data = studyStateRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("study not exist");
        }
    }

    @Override
    public StudyStateEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<StudyStateEntity> findAll() {
        if (!studyStateRepository.findAll().isEmpty()) {
            return studyStateRepository.findAll();
        } else {
            throw new NotFoundException("not exist any study ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<StudyStateEntity> data = studyStateRepository.findById(id);
        if (data.isPresent()) {
            studyStateRepository.deleteById(id);
        } else {
            throw new NotFoundException("study not exist");
        }
    }

    @Override
    public StudyStateEntity create(StudyStateEntity data) {
        Optional<StudyStateEntity> study = studyStateRepository.findByName(data.getName());
        if (!study.isPresent()) {
            return studyStateRepository.save(data);
        } else {
            throw new FoundException("study already exist");
        }
    }

    @Override
    public StudyStateEntity update(Long id, StudyStateEntity data) {
        Optional<StudyStateEntity> study = studyStateRepository.findById(id);
        if (study.isPresent()) {
            StudyStateEntity updatedStudy = study.get();
            updatedStudy.setName(data.getName());
            updatedStudy.setCode(data.getCode());
            updatedStudy.setDescription(data.getDescription());
            updatedStudy.setActive(data.isActive());
            updatedStudy.setUpdatedAt(data.getUpdatedAt());
            return studyStateRepository.save(updatedStudy);
        } else {
            throw new NotFoundException("study not found");
        }
    }
}
