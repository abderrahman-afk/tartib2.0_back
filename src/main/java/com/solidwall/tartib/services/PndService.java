package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.pnd.PndDto;
import com.solidwall.tartib.entities.PndEntity;
import com.solidwall.tartib.implementations.PndImplementation;
import com.solidwall.tartib.repositories.PndRepository;

@Service
public class PndService implements PndImplementation {

    @Autowired
    PndRepository pndRepository;

    @Override
    public PndDto getOne(Long id) {
        Optional<PndEntity> data = pndRepository.findById(id);
        if (data.isPresent()) {
            PndDto pnd = new PndDto();
            pnd.setId(data.get().getId());
            pnd.setName(data.get().getName());
            pnd.setCode(data.get().getCode());
            pnd.setDescription(data.get().getDescription());
            pnd.setActive(data.get().isActive());
            pnd.setCreatedAt(data.get().getCreatedAt());
            pnd.setUpdatedAt(data.get().getUpdatedAt());

            return pnd;
        } else {
            throw new NotFoundException("PND not exist");
        }
    }

    @Override
    public PndDto findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<PndDto> findAll() {
        if (!pndRepository.findAll().isEmpty()) {
            List<PndEntity> pnds= pndRepository.findAll();
            List<PndDto> pndDtos = new ArrayList<>();
            for (PndEntity pnd : pnds) {
                PndDto pndDto = new PndDto();
                pndDto.setId(pnd.getId());
                pndDto.setName(pnd.getName());
                pndDto.setCode(pnd.getCode());
                pndDto.setDescription(pnd.getDescription());
                pndDto.setActive(pnd.isActive());
                pndDto.setCreatedAt(pnd.getCreatedAt());
                pndDto.setUpdatedAt(pnd.getUpdatedAt());
                pndDtos.add(pndDto);
            }
            return pndDtos;
        } else {
            throw new NotFoundException("not exist any PND ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<PndEntity> data = pndRepository.findById(id);
        if (data.isPresent()) {
            pndRepository.deleteById(id);
        } else {
            throw new NotFoundException("PND not exist");
        }
    }

    @Override
    public PndDto create(PndEntity data) {
        Optional<PndEntity> pnd = pndRepository.findByName(data.getName());
        if (!pnd.isPresent()) {
            PndEntity savPndEntity = pndRepository.save(data);
            PndDto pndDto = new PndDto();
            pndDto.setId(savPndEntity.getId());
            pndDto.setName(savPndEntity.getName());
            pndDto.setCode(savPndEntity.getCode());
            pndDto.setDescription(savPndEntity.getDescription());
            pndDto.setActive(savPndEntity.isActive());
            pndDto.setCreatedAt(savPndEntity.getCreatedAt());
            pndDto.setUpdatedAt(savPndEntity.getUpdatedAt());
            return pndDto;
        } else {
            throw new FoundException("PND already exist");
        }
    }

    @Override
    public PndDto update(Long id, PndEntity data) {
        Optional<PndEntity> pnd = pndRepository.findById(id);
        if (pnd.isPresent()) {
            PndEntity updatedPnd = pnd.get();
            updatedPnd.setName(data.getName());
            updatedPnd.setCode(data.getCode());
            updatedPnd.setDescription(data.getDescription());
            updatedPnd.setActive(data.isActive());
            updatedPnd.setCreatedAt(data.getCreatedAt());
            updatedPnd.setUpdatedAt(data.getUpdatedAt());
            updatedPnd = pndRepository.save(updatedPnd);

            PndDto pndDto = new PndDto();
            pndDto.setId(updatedPnd.getId());
            pndDto.setName(updatedPnd.getName());
            pndDto.setCode(updatedPnd.getCode());
            pndDto.setDescription(updatedPnd.getDescription());
            pndDto.setActive(updatedPnd.isActive());
            pndDto.setCreatedAt(updatedPnd.getCreatedAt());
            pndDto.setUpdatedAt(updatedPnd.getUpdatedAt());
            return pndDto;
        } else {
            throw new NotFoundException("PND not found");
        }
    }

}
