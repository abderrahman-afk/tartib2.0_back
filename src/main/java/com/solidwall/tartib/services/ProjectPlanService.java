package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.plan.CreateDto;
import com.solidwall.tartib.dto.project.plan.UpdateDto;
import com.solidwall.tartib.entities.FundingSourceTypeEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectLogicEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import com.solidwall.tartib.entities.RubriqueEntity;
import com.solidwall.tartib.entities.StakeHolderEntity;
import com.solidwall.tartib.entities.FinancialSourceEntity;
import com.solidwall.tartib.implementations.ProjectPlanImplementation;
import com.solidwall.tartib.repositories.FinancialSourceRepository;
import com.solidwall.tartib.repositories.FundingSourceRepository;
import com.solidwall.tartib.repositories.FundingSourceTypeRepository;
import com.solidwall.tartib.repositories.ProjectIdentityRepository;
import com.solidwall.tartib.repositories.ProjectLogicRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;
import com.solidwall.tartib.repositories.ProjectScaleRepository;
import com.solidwall.tartib.repositories.RubriqueRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectPlanService implements ProjectPlanImplementation {

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;

  @Autowired
  ProjectPlanRepository projectPlanRepository;

  @Autowired
  FinancialSourceRepository financialSourceRepository;

  @Autowired
  RubriqueRepository rubriqueRepository;

  @Autowired
  ProjectScaleRepository projectScaleRepository;

  @Autowired
  ProjectScaleService prokScaleService;

  @Override
  public List<ProjectPlanEntity> findAll() {
    if (!projectPlanRepository.findAll().isEmpty()) {
      return projectPlanRepository.findAll();
    } else {
      throw new NotFoundException("not exist any project plan ");
    }
  }

  @Override
  public ProjectPlanEntity getOne(Long id) {
    Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(id);
    if (projectPlan.isPresent()) {
      return projectPlan.get();
    } else {
      throw new NotFoundException("project plan not exist");
    }
  }

  @Override
  public ProjectPlanEntity findOne(Map<String, String> data) {
    if (data.get("projectIdentity") != null) {
      Long projectId = Long.parseLong(data.get("projectIdentity"));
      Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(projectId);
      if (!projectIdentity.isPresent())
        // throw new NotFoundException("project plan not found");
        return null;
      Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findByProjectIdentity(projectIdentity.get());
      // if (!projectPlan.isPresent())
      //   throw new NotFoundException("project plan not found");
      return projectPlan.orElse(null);
    }
    throw new BadRequestException("param not exist");
  }

  @Override
  public ProjectPlanEntity create(CreateDto data) {

    Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());

    if (project.isPresent()) {
      ProjectPlanEntity newProjectPlan = new ProjectPlanEntity();
      newProjectPlan.setProjectIdentity(project.get());
      newProjectPlan.setCoutTotale(data.getCoutTotale());
      newProjectPlan.setCoutDinars(data.getCoutDinars());
      newProjectPlan.setMontantAnnuel(data.getMontantAnnuel());
      newProjectPlan.setObservation(data.getObservation());
      newProjectPlan.setTauxEchange(data.getTauxEchange());

      if (data.getFinancialSource() != null) {
        List<FinancialSourceEntity> financialSources = data.getFinancialSource().stream()
            .map(financialSourceDto -> {
              FinancialSourceEntity financialSource = new FinancialSourceEntity();
              financialSource.setBailleur(financialSourceDto.getBailleur());
              financialSource.setStatut(financialSourceDto.getStatut());
              financialSource.setTauxEchange(financialSourceDto.getTauxEchange());
              financialSource.setDevise(financialSourceDto.getDevise());
              financialSource.setType(financialSourceDto.getType());
              financialSource.setMontant(financialSourceDto.getMontant());
              financialSource.setMontantDinars(financialSourceDto.getMontantDinars());
              financialSource.setProjectPlan(newProjectPlan);
              return financialSource;
            })
            .collect(Collectors.toList());
        newProjectPlan.setFinancialSource(financialSources);
      }

      if (data.getRubriques() != null) {
        List<RubriqueEntity> rubriques = data.getRubriques().stream()
            .map(rubriqueDTO -> {
              RubriqueEntity rub = new RubriqueEntity();
              rub.setAmount(rubriqueDTO.getAmount());
              rub.setName(rubriqueDTO.getName());
              rub.setProjectPlan(newProjectPlan);
              return rub;
            })
            .collect(Collectors.toList());
        newProjectPlan.setRubriques(rubriques);
      }

      ProjectScaleEntity scale = projectScaleRepository
          .findByMinimumBudgetLessThanEqualAndMaximumBudgetGreaterThanEqual(data.getCoutTotale(), data.getCoutTotale());
      List<ProjectScaleEntity> projectScaleEntitycheck = projectScaleRepository
          .findByMaximumBudgetAndMinimumBudget(scale.getMinimumBudget(), scale.getMaximumBudget());

      if (!projectScaleEntitycheck.isEmpty() && !projectScaleEntitycheck.contains(scale) ) {
        throw new FoundException("cet intervale intersect avec un autre intervalle du budget");
      }

      ProjectIdentityEntity identity = projectIdentityRepository.findById(data.getProjectIdentity()).get();

      scale.setProjectIdentity(identity);
      projectScaleRepository.save(scale);

      Optional<ProjectPlanEntity> ProjectPlan = projectPlanRepository.findByProjectIdentity(identity);
      if (ProjectPlan.isPresent()) {
        projectPlanRepository.delete(ProjectPlan.get());
      }

      return projectPlanRepository.save(newProjectPlan);
    } else {
      throw new NotFoundException("project not found");
    }

  }

  @Override
  @Transactional
  public ProjectPlanEntity update(Long id, UpdateDto data) {
      Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(id);
      Optional<ProjectIdentityEntity> project = projectIdentityRepository.findById(data.getProjectIdentity());
  
      if (projectPlan.isPresent() && project.isPresent()) {
          ProjectPlanEntity updateProjectPlan = projectPlan.get();
          
          // Basic fields update
          updateProjectPlan.setProjectIdentity(project.get());
          updateProjectPlan.setCoutTotale(data.getCoutTotale());
          updateProjectPlan.setCoutDinars(data.getCoutDinars());
          updateProjectPlan.setMontantAnnuel(data.getMontantAnnuel());
          updateProjectPlan.setObservation(data.getObservation());
          updateProjectPlan.setTauxEchange(data.getTauxEchange());
  
          // Handle financial sources - ONE approach, not both
          if (data.getFinancialSource() != null) {
              // First, remove old associations 
              financialSourceRepository.deleteByProjectPlanId(updateProjectPlan.getId());
              
              // Now make a new collection - don't modify the existing one
              List<FinancialSourceEntity> financialSources = data.getFinancialSource().stream()
                  .map(financialSourceDto -> {
                      FinancialSourceEntity financialSource = new FinancialSourceEntity();
                      financialSource.setStatut(financialSourceDto.getStatut());
                      financialSource.setTauxEchange(financialSourceDto.getTauxEchange());
                      financialSource.setBailleur(financialSourceDto.getBailleur());
                      financialSource.setType(financialSourceDto.getType());
                      financialSource.setDevise(financialSourceDto.getDevise());
                      financialSource.setMontant(financialSourceDto.getMontant());
                      financialSource.setMontantDinars(financialSourceDto.getMontantDinars());
                      financialSource.setProjectPlan(updateProjectPlan);
                      return financialSource;
                  })
                  .collect(Collectors.toList());
              
              // Replace the collection entirely
              updateProjectPlan.setFinancialSource(financialSources);
          }
  
          // Similar approach for rubriques
          if (data.getRubriques() != null) {
              // First, remove old associations
              rubriqueRepository.deleteByProjectPlanId(updateProjectPlan.getId());
              
              // Create new collection
              List<RubriqueEntity> rubriques = data.getRubriques().stream()
                  .map(rubriqueDTO -> {
                      RubriqueEntity rub = new RubriqueEntity();
                      rub.setAmount(rubriqueDTO.getAmount());
                      rub.setName(rubriqueDTO.getName());
                      rub.setProjectPlan(updateProjectPlan);
                      return rub;
                  })
                  .collect(Collectors.toList());
              
              // Replace the collection entirely
              updateProjectPlan.setRubriques(rubriques);
          }
  
          // Rest of your existing code...
          
          return projectPlanRepository.save(updateProjectPlan);
      } else {
          throw new NotFoundException("project plan not found");
      }
  }
  
  @Override
  public void delete(Long id) {
    Optional<ProjectPlanEntity> projectPlan = projectPlanRepository.findById(id);
    if (projectPlan.isPresent()) {
      projectPlanRepository.deleteById(id);
    } else {
      throw new NotFoundException("project plan not exist");
    }
  }
}
