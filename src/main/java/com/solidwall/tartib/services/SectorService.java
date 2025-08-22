package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.sector.CreateDto;
import com.solidwall.tartib.dto.sector.UpdateDto;
import com.solidwall.tartib.entities.SectorEntity;
import com.solidwall.tartib.implementations.SectorImplementation;
import com.solidwall.tartib.repositories.SectorRepository;

@Service
public class SectorService implements SectorImplementation {

    @Autowired
    SectorRepository sectorRepository;

    @Override
    public List<SectorEntity> findAll() {
        if (!sectorRepository.findAll().isEmpty()) {
            return sectorRepository.findAll();
        } else {
            throw new NotFoundException("not exist any sector");
        }
    }

    @Override
    public SectorEntity getOne(Long id) {
        Optional<SectorEntity> sector = sectorRepository.findById(id);
        if (sector.isPresent()) {
            return sector.get();
        } else {
            throw new NotFoundException("sector not exist");
        }
    }

    @Override
    public SectorEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public SectorEntity create(CreateDto data) {
        Optional<SectorEntity> sector = sectorRepository.findByName(data.getName());
        if (!sector.isPresent()) {
            SectorEntity newSector = new SectorEntity();
            newSector.setName(data.getName());
            newSector.setCode(data.getCode());
            newSector.setDescription(data.getDescription());
            newSector.setActive(data.isActive());
            return sectorRepository.save(newSector);
        } else {
            throw new FoundException("sector already exist");
        }

    }

    @Override
    public SectorEntity update(Long id, UpdateDto data) {

        Optional<SectorEntity> sector = sectorRepository.findById(id);

        if (sector.isPresent()) {
            SectorEntity updatedSector = sector.get();
            updatedSector.setName(data.getName());
            updatedSector.setCode(data.getCode());
            updatedSector.setDescription(data.getDescription());
            updatedSector.setActive(data.isActive());
            return sectorRepository.save(updatedSector);
        } else {
            throw new NotFoundException("sector not found");
        }

    }

    @Override
    public void delete(Long id) {
        Optional<SectorEntity> sector = sectorRepository.findById(id);
        if (sector.isPresent()) {
            sectorRepository.deleteById(id);
        } else {
            throw new NotFoundException("sector not exist");
        }
    }

}
