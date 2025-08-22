package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.CategoryRiskEntity;
import com.solidwall.tartib.implementations.CategoryRiskImplementation;
import com.solidwall.tartib.repositories.CategoryRiskRepository;

@Service
public class CategoryRiskService implements CategoryRiskImplementation {

    @Autowired
    CategoryRiskRepository categoryriskRepository;

    @Override
    public CategoryRiskEntity getOne(Long id) {
        Optional<CategoryRiskEntity> data = categoryriskRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("category risk  not exist");
        }
    }

    @Override
    public CategoryRiskEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<CategoryRiskEntity> findAll() {
        if (!categoryriskRepository.findAll().isEmpty()) {
            return categoryriskRepository.findAll();
        } else {
            throw new NotFoundException("not exist any category risk ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<CategoryRiskEntity> data = categoryriskRepository.findById(id);
        if (data.isPresent()) {
            categoryriskRepository.deleteById(id);
        } else {
            throw new NotFoundException("category not exist");
        }
    }

    @Override
    public CategoryRiskEntity create(CategoryRiskEntity data) {
        Optional<CategoryRiskEntity> category = categoryriskRepository.findByName(data.getName());
        if (!category.isPresent()) {
            return categoryriskRepository.save(data);
        } else {
            throw new FoundException("category risk already exist");
        }
    }

    @Override
    public CategoryRiskEntity update(Long id, CategoryRiskEntity data) {
        Optional<CategoryRiskEntity> category = categoryriskRepository.findById(id);
        if (category.isPresent()) {
            CategoryRiskEntity updatedCat = category.get();
            updatedCat.setName(data.getName());
            updatedCat.setCode(data.getCode());
            updatedCat.setDescription(data.getDescription());
            updatedCat.setActive(data.isActive());
            updatedCat.setUpdatedAt(data.getUpdatedAt());
            return categoryriskRepository.save(updatedCat);
        } else {
            throw new NotFoundException("category risk not found");
        }
    }
}
