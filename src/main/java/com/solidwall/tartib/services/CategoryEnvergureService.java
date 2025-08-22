package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.CategoryEnvergureEntity;
import com.solidwall.tartib.implementations.CategoryEnvergureImplementation;
import com.solidwall.tartib.repositories.CategoryEnvergureRepository;

@Service
public class CategoryEnvergureService implements CategoryEnvergureImplementation{

    @Autowired
    CategoryEnvergureRepository categoryEnvergureRepository;

    @Override
    public CategoryEnvergureEntity getOne(Long id) {
        Optional<CategoryEnvergureEntity> data = categoryEnvergureRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("envergure not exist");
        }
    }

    @Override
    public CategoryEnvergureEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<CategoryEnvergureEntity> findAll() {
        if (!categoryEnvergureRepository.findAll().isEmpty()) {
            return categoryEnvergureRepository.findAll();
        } else {
            throw new NotFoundException("not exist any envergure");
        }
    }

    @Override
    public CategoryEnvergureEntity create(CategoryEnvergureEntity data) {
        Optional<CategoryEnvergureEntity> projectCategoryEnvergure = categoryEnvergureRepository.findByName(data.getName());
        if (!projectCategoryEnvergure.isPresent()) {
            return categoryEnvergureRepository.save(data);
        } else {
            throw new FoundException("envergure already exist");
        }
    }

    @Override
    public CategoryEnvergureEntity update(Long id, CategoryEnvergureEntity data) {
        Optional<CategoryEnvergureEntity> envergure = categoryEnvergureRepository.findById(id);
        if (envergure.isPresent()) {
            CategoryEnvergureEntity updateCategoryEnvergure = envergure.get();
            updateCategoryEnvergure.setName(data.getName());
            updateCategoryEnvergure.setCode(data.getCode());
            updateCategoryEnvergure.setDescription(data.getDescription());
            updateCategoryEnvergure.setActive(data.isActive());
            updateCategoryEnvergure.setUpdatedAt(data.getUpdatedAt());
            return categoryEnvergureRepository.save(updateCategoryEnvergure);
        } else {
            throw new NotFoundException("envergure not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<CategoryEnvergureEntity> data = categoryEnvergureRepository.findById(id);
        if (data.isPresent()) {
            categoryEnvergureRepository.deleteById(id);
        } else {
            throw new NotFoundException("envergure not exist");
        }
    }
}
