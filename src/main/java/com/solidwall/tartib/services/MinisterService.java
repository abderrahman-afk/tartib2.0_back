package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.implementations.MinisterImplementation;
import com.solidwall.tartib.repositories.MinisterRepository;

@Service
public class MinisterService implements MinisterImplementation {

    @Autowired
    MinisterRepository ministerRepository;

    @Override
    public MinisterEntity getOne(Long id) {
        Optional<MinisterEntity> data = ministerRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("minister not exist");
        }
    }

    @Override
    public MinisterEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<MinisterEntity> findAll() {
        if (!ministerRepository.findAll().isEmpty()) {
            return ministerRepository.findAll();
        } else {
            throw new NotFoundException("not exist any minister ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<MinisterEntity> data = ministerRepository.findById(id);
        if (data.isPresent()) {
            ministerRepository.deleteById(id);
        } else {
            throw new NotFoundException("minister not exist");
        }
    }

    @Override
    public MinisterEntity create(MinisterEntity data) {
        Optional<MinisterEntity> minister = ministerRepository.findByName(data.getName());
        if (!minister.isPresent()) {
            return ministerRepository.save(data);
        } else {
            throw new FoundException("minister already exist");
        }
    }

    @Override
    public MinisterEntity update(Long id, MinisterEntity data) {
        Optional<MinisterEntity> minister = ministerRepository.findById(id);
        if (minister.isPresent()) {
            MinisterEntity updatedMinister = minister.get();
            updatedMinister.setName(data.getName());
            updatedMinister.setCode(data.getCode());
            updatedMinister.setDescription(data.getDescription());
            updatedMinister.setActive(data.isActive());
            updatedMinister.setUpdatedAt(data.getUpdatedAt());
            return ministerRepository.save(updatedMinister);
        } else {
            throw new NotFoundException("minister not found");
        }
    }
}
