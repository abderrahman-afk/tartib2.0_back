package com.solidwall.tartib.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.gridbonus.CreateGridBonusDto;
import com.solidwall.tartib.dto.gridbonus.GridBonusLevelDto;
import com.solidwall.tartib.dto.gridbonus.GridBonusResponseDto;
import com.solidwall.tartib.entities.ClassificationEntity;
import com.solidwall.tartib.entities.EvaluationCritiriaEntity;
import com.solidwall.tartib.entities.EvaluationIndicateurEntity;
import com.solidwall.tartib.entities.GridBonusEntity;
import com.solidwall.tartib.entities.GridBonusLevelEntity;
import com.solidwall.tartib.implementations.GridBonusImplementation;
import com.solidwall.tartib.repositories.ClassificationRepository;
import com.solidwall.tartib.repositories.EvaluationCritiriaRepository;
import com.solidwall.tartib.repositories.EvaluationIndicateurRepository;
import com.solidwall.tartib.repositories.GridBonusRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional
@Slf4j
public class GridBonusService implements GridBonusImplementation {
    
    @Autowired
    private GridBonusRepository gridBonusRepository;
    
    @Autowired
    private ClassificationRepository classificationRepository;
    
    @Autowired
    private EvaluationGridService evaluationGridService;
    
    @Autowired
    private EvaluationCritiriaRepository critiriaRepository;
    
    @Autowired
    private EvaluationIndicateurRepository indicateurRepository;

    @Override
    public GridBonusResponseDto create(CreateGridBonusDto data) {
        log.info("Creating grid bonus with code: {}", data.getCode());

        // Validate classification exists
        ClassificationEntity classification = classificationRepository.findById(data.getClassificationId())
            .orElseThrow(() -> new NotFoundException("Classification not found"));

        // Validate criteria exists
        EvaluationCritiriaEntity criteria = critiriaRepository.findById(data.getCriteriaId())
            .orElseThrow(() -> new NotFoundException("Criteria not found"));

        // Validate indicator if provided
        EvaluationIndicateurEntity indicator = null;
        if (data.getIndicatorId() != null) {
            indicator = indicateurRepository.findById(data.getIndicatorId())
                .orElseThrow(() -> new NotFoundException("Indicator not found"));
                
            // Validate indicator belongs to criteria
            if (!indicator.getEvaluationCritiria().getId().equals(criteria.getId())) {
                throw new BadRequestException("Indicator does not belong to the selected criteria");
            }
        }

        // Create grid bonus entity
        GridBonusEntity gridBonus = new GridBonusEntity();
        gridBonus.setCode(data.getCode());
        gridBonus.setName(data.getName());
        gridBonus.setClassification(classification);
        gridBonus.setCriteria(criteria);
        gridBonus.setIndicator(indicator);

        // Create and set levels
        List<GridBonusLevelEntity> levels = data.getLevels().stream()
            .map(levelDto -> {
                GridBonusLevelEntity level = new GridBonusLevelEntity();
                level.setGridBonus(gridBonus);
                level.setLevelName(levelDto.getLevelName());
                level.setPointsThreshold(levelDto.getPointsThreshold());
                level.setBonusPercentage(levelDto.getBonusPercentage());
                level.setComment(levelDto.getComment());
                return level;
            })
            .collect(Collectors.toList());

        gridBonus.setLevels(levels);

        // Save and return
        GridBonusEntity savedBonus = gridBonusRepository.save(gridBonus);
        return mapToDto(savedBonus);
    }

    @Override
    public GridBonusResponseDto update(Long id, CreateGridBonusDto data) {
        GridBonusEntity gridBonus = gridBonusRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Grid bonus not found"));

        // Update basic fields
        gridBonus.setCode(data.getCode());
        gridBonus.setName(data.getName());

        // Update criteria and indicator
        EvaluationCritiriaEntity criteria = critiriaRepository.findById(data.getCriteriaId())
            .orElseThrow(() -> new NotFoundException("Criteria not found"));
        gridBonus.setCriteria(criteria);

        if (data.getIndicatorId() != null) {
            EvaluationIndicateurEntity indicator = indicateurRepository.findById(data.getIndicatorId())
                .orElseThrow(() -> new NotFoundException("Indicator not found"));
            gridBonus.setIndicator(indicator);
        } else {
            gridBonus.setIndicator(null);
        }

        // Update levels
        gridBonus.getLevels().clear();
        List<GridBonusLevelEntity> newLevels = data.getLevels().stream()
            .map(levelDto -> {
                GridBonusLevelEntity level = new GridBonusLevelEntity();
                level.setGridBonus(gridBonus);
                level.setLevelName(levelDto.getLevelName());
                level.setPointsThreshold(levelDto.getPointsThreshold());
                level.setBonusPercentage(levelDto.getBonusPercentage());
                level.setComment(levelDto.getComment());
                return level;
            })
            .collect(Collectors.toList());
        gridBonus.getLevels().addAll(newLevels);

        return mapToDto(gridBonusRepository.save(gridBonus));
    }

    @Override
    public GridBonusResponseDto getOne(Long id) {
        return mapToDto(gridBonusRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Grid bonus not found")));
    }

    @Override
    public List<GridBonusResponseDto> findByClassification(Long classificationId) {
        return gridBonusRepository.findByClassificationId(classificationId).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        GridBonusEntity gridBonus = gridBonusRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Grid bonus not found"));
        gridBonusRepository.delete(gridBonus);
    }

    private GridBonusResponseDto mapToDto(GridBonusEntity entity) {
        GridBonusResponseDto dto = new GridBonusResponseDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCriteriaName(entity.getCriteria().getName());
        
        if (entity.getIndicator() != null) {
            dto.setIndicatorName(entity.getIndicator().getName());
        }
    
        List<GridBonusLevelDto> levels = entity.getLevels().stream()
            .map(level -> {
                GridBonusLevelDto levelDto = new GridBonusLevelDto();
                levelDto.setLevelName(level.getLevelName());
                levelDto.setPointsThreshold(level.getPointsThreshold());
                levelDto.setBonusPercentage(level.getBonusPercentage());
                levelDto.setComment(level.getComment());
                return levelDto;
            })
            .collect(Collectors.toList());
    
        dto.setLevels(levels);
        return dto;
    }
}
    

