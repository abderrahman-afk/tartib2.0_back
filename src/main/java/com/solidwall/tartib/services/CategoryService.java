package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.CategoryEntity;
import com.solidwall.tartib.implementations.CategoryImplementation;
import com.solidwall.tartib.repositories.CategoryRepository;

@Service
public class CategoryService implements CategoryImplementation {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryEntity getOne(Long id) {
        Optional<CategoryEntity> data = categoryRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("category not exist");
        }
    }

    @Override
    public CategoryEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<CategoryEntity> findAll() {
        if (!categoryRepository.findAll().isEmpty()) {
            return categoryRepository.findAll();
        } else {
            throw new NotFoundException("not exist any category ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<CategoryEntity> data = categoryRepository.findById(id);
        if (data.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundException("category not exist");
        }
    }

    @Override
    public CategoryEntity create(CategoryEntity data) {
        Optional<CategoryEntity> category = categoryRepository.findByName(data.getName());
        if (!category.isPresent()) {
            return categoryRepository.save(data);
        } else {
            throw new FoundException("category already exist");
        }
    }

    @Override
    public CategoryEntity update(Long id, CategoryEntity data) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            CategoryEntity updatedCat = category.get();
            updatedCat.setName(data.getName());
            updatedCat.setCode(data.getCode());
            updatedCat.setDescription(data.getDescription());
            updatedCat.setActive(data.isActive());
            updatedCat.setUpdatedAt(data.getUpdatedAt());
            return categoryRepository.save(updatedCat);
        } else {
            throw new NotFoundException("category not found");
        }
    }
}
