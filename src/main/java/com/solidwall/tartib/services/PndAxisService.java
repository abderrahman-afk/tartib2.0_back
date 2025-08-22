package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.pndAxe.CreateDto;
import com.solidwall.tartib.dto.pndAxe.PndAxisResponseDto;
import com.solidwall.tartib.dto.pndAxe.UpdateDto;
import com.solidwall.tartib.entities.PndAxisEntity;
import com.solidwall.tartib.entities.PndEntity;
import com.solidwall.tartib.implementations.PndAxisImplementation;
import com.solidwall.tartib.repositories.PndAxisRepository;
import com.solidwall.tartib.repositories.PndRepository;

@Service
public class PndAxisService implements PndAxisImplementation {

    @Autowired
    PndAxisRepository pndAxisRepository;
    @Autowired
    PndRepository pndRepository;

    @Override
    public PndAxisResponseDto getOne(Long id) {
        Optional<PndAxisEntity> data = pndAxisRepository.findById(id);
        if (data.isPresent()) {
            return PndAxisResponseDto.fromEntity(data.get());
        } else {
            throw new NotFoundException("pnd axis not exist");
        }
    }

    @Override
    public PndAxisEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<PndAxisEntity> findAll() {
        if (!pndAxisRepository.findAll().isEmpty()) {
            return pndAxisRepository.findAll();
        } else {
            throw new NotFoundException("not exist any pnd axis ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<PndAxisEntity> data = pndAxisRepository.findById(id);
        if (data.isPresent()) {
            pndAxisRepository.deleteById(id);
        } else {
            throw new NotFoundException("pnd axis not exist");
        }
    }

    @Override
    public PndAxisEntity create(CreateDto data) {
        Optional<PndAxisEntity> pndAx = pndAxisRepository.findByName(data.getName());
        if (!pndAx.isPresent()) {
            Optional<PndEntity> pnd = pndRepository.findById(data.getPnd());
            if (!pnd.isPresent()) {
                throw new NotFoundException("pnd not found");
            }
            PndAxisEntity pndAxe = new PndAxisEntity();
            pndAxe.setName(data.getName());
            pndAxe.setCode(data.getCode());
            pndAxe.setDescription(data.getDescription());
            pndAxe.setPnd(pnd.get());
            pndAxe.setActive(data.isActive());
            return pndAxisRepository.save(pndAxe);
        } else {
            throw new FoundException("pnd axis already exist");
        }
    }

    @Override
    public PndAxisEntity update(Long id, UpdateDto data) {
        Optional<PndAxisEntity> pndAxis = pndAxisRepository.findById(id);
        if (pndAxis.isPresent()) {
            Optional<PndEntity> pnd = pndRepository.findById(data.getPnd());
            if (!pnd.isPresent()) {
                throw new NotFoundException("pnd not found");
            }
            PndAxisEntity updatedPndAxis = pndAxis.get();
            updatedPndAxis.setName(data.getName());
            updatedPndAxis.setCode(data.getCode());
            updatedPndAxis.setDescription(data.getDescription());
            updatedPndAxis.setActive(data.isActive());
            updatedPndAxis.setPnd(pnd.get());
            return pndAxisRepository.save(updatedPndAxis);
        } else {
            throw new NotFoundException("pnd axis not found");
        }
    }

    @Override
    public List<PndAxisEntity> findByPnd(Long id) {
        Optional<PndEntity> pnd = pndRepository.findById(id);
        if(pnd.isPresent()) {
            List<PndAxisEntity> pndAxes = pndAxisRepository.findByPnd(pnd.get());
            if (!pndAxes.isEmpty()) {
                return pndAxes;
            } else {
                throw new NotFoundException("not exist any pnd-axis for this pnd");
            }
        } else {
            throw new NotFoundException("pnd not exist");
        }
    }
}