package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.TypologyEntity;
import com.solidwall.tartib.implementations.TypologyImplementation;
import com.solidwall.tartib.repositories.TypologyRepository;

@Service
public class TypologyService implements TypologyImplementation {

    @Autowired
    TypologyRepository typologyRepository;

    @Override
    public TypologyEntity getOne(Long id) {
        Optional<TypologyEntity> data = typologyRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("typology not exist");
        }
    }

    @Override
    public TypologyEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<TypologyEntity> findAll() {
        if (!typologyRepository.findAll().isEmpty()) {
            return typologyRepository.findAll();
        } else {
            throw new NotFoundException("not exist any typology");
        }
    }

    @Override
    public TypologyEntity create(TypologyEntity data) {
        Optional<TypologyEntity> projectTypology = typologyRepository.findByName(data.getName());
        if (!projectTypology.isPresent()) {
            return typologyRepository.save(data);
        } else {
            throw new FoundException("typology already exist");
        }
    }

    @Override
    public TypologyEntity update(Long id, TypologyEntity data) {
        Optional<TypologyEntity> typology = typologyRepository.findById(id);
        if (typology.isPresent()) {
            TypologyEntity updateTypology = typology.get();
            updateTypology.setName(data.getName());
            updateTypology.setCode(data.getCode());
            updateTypology.setDescription(data.getDescription());
            updateTypology.setActive(data.isActive());
            updateTypology.setUpdatedAt(data.getUpdatedAt());
            return typologyRepository.save(updateTypology);
        } else {
            throw new NotFoundException("typology not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<TypologyEntity> data = typologyRepository.findById(id);
        if (data.isPresent()) {
            typologyRepository.deleteById(id);
        } else {
            throw new NotFoundException("typology not exist");
        }
    }

}
