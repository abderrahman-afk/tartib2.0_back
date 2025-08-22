package com.solidwall.tartib.implementations;

import java.util.List;
import java.util.Map;


import com.solidwall.tartib.dto.classementPortfolio.ClassificationResponseDto;
import com.solidwall.tartib.dto.classementPortfolio.CreateClassificationDto;
import com.solidwall.tartib.dto.classementPortfolio.UpdateClassificationDto;

public interface ClassificationImplementation { 
     ClassificationResponseDto create(CreateClassificationDto data);
    ClassificationResponseDto update(Long id, UpdateClassificationDto data);
    ClassificationResponseDto getOne(Long id);
    List<ClassificationResponseDto> findAll(Map<String, String> filters);
    void activate(Long id);
    void delete(Long id);
    void deactivateAll();
    ClassificationResponseDto findActive();
}
