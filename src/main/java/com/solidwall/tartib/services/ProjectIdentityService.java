package com.solidwall.tartib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.project.identity.CreateDto;
import com.solidwall.tartib.dto.project.identity.UpdateDto;
import com.solidwall.tartib.entities.CategoryEntity;
import com.solidwall.tartib.entities.CategoryEnvergureEntity;
import com.solidwall.tartib.entities.MinisterEntity;
import com.solidwall.tartib.entities.ProjectBlockEntity;
import com.solidwall.tartib.entities.ProjectEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.entities.SectorEntity;
import com.solidwall.tartib.entities.TypologyEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.enums.ProjectStaut;
import com.solidwall.tartib.implementations.ProjectIdentityImplementation;
import com.solidwall.tartib.repositories.*;
import jakarta.transaction.Transactional;

@Service
public class ProjectIdentityService implements ProjectIdentityImplementation {

  @Autowired
  ProjectIdentityRepository projectIdentityRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TypologyRepository typologyRepository;

  @Autowired
  MinisterRepository ministerRepository;

  @Autowired
  SectorRepository sectorRepository;
  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  OrganisationRepository organisationRepository;
  @Autowired
  CategoryEnvergureRepository categoryEnvergureRepository;

  @Autowired
  private ProjectZoneRepository projectZoneRepository;

  @Autowired
  private ProjectPlanRepository projectPlanRepository;

  @Autowired
  private ProjectStrategyRepository projectStrategyRepository;

  @Autowired
  private ProjectLogicRepository projectLogicRepository;

  @Autowired
  private ProjectRiskRepository projectRiskRepository;

  @Autowired
  private projectBlockRepository projectBlockRepository;
  @Autowired
  private AuthenticationFacade authenticationFacade;
  @Autowired
  private UserRepository userRepository;

  // ENHANCED findAll() METHOD:
  @Override
  public List<ProjectIdentityEntity> findAll(Boolean archived) {
    // Get current user and their accessible ministries
    Long currentUserId = authenticationFacade.getCurrentUserId();
    List<Long> accessibleMinistryIds = getAccessibleMinistryIds(currentUserId);

    
    // Get all projects first
    List<ProjectIdentityEntity> allProjects = projectIdentityRepository.findAll().stream()
        .filter(project -> archived == null || project.isArchived() == archived)
        .collect(Collectors.toList());

    if (allProjects.isEmpty()) {
      return new ArrayList<>(); // Return empty list if no projects found
    }

    // Filter projects based on ministry access
    List<ProjectIdentityEntity> filteredProjects = allProjects.stream()
        .filter(project -> isProjectAccessible(project, accessibleMinistryIds))
        .collect(Collectors.toList());

    if (filteredProjects.isEmpty()) {
      return new ArrayList<>(); // Return empty list if no projects found
    }

    return filteredProjects;
  }

  // NEW HELPER METHODS:
  private List<Long> getAccessibleMinistryIds(Long userId) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));

    List<Long> accessibleIds = new ArrayList<>();

    switch (user.getMinistryAccessType()) {
      case ALL_MINISTRIES:
        // Return all active ministry IDs
        return ministerRepository.findAll().stream()
            .filter(MinisterEntity::isActive)
            .map(MinisterEntity::getId)
            .collect(Collectors.toList());

      case OWN_MINISTRY:
        // Return only user's own ministry
        if (user.getMinistry() != null) {
          accessibleIds.add(user.getMinistry().getId());
        }
        break;

      case REFERENCE_MINISTRIES:
        // Return own ministry + reference ministries
        if (user.getMinistry() != null) {
          accessibleIds.add(user.getMinistry().getId());
        }
        user.getReferenceMinistries().forEach(ministry -> accessibleIds.add(ministry.getId()));
        break;

      default:
        // If no access type is set, default to own ministry only
        if (user.getMinistry() != null) {
          accessibleIds.add(user.getMinistry().getId());
        }
        break;
    }

    return accessibleIds;
  }

  private boolean isProjectAccessible(ProjectIdentityEntity project, List<Long> accessibleMinistryIds) {
    // If user has access to all ministries, return true
    if (accessibleMinistryIds.isEmpty()) {
      return false;
    }

    // If project has no minister assigned, it might be accessible based on business
    // rules
    if (project.getMinister() == null) {
      return true; // Or false, depending on your business logic
    }

    // Check if project's ministry is in user's accessible ministries
    return accessibleMinistryIds.contains(project.getMinister().getId());
  }

  @Override
  public ProjectIdentityEntity getOne(Long id) {
    Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(id);
    if (projectIdentity.isPresent()) {
      return projectIdentity.get();
    } else {
      throw new NotFoundException("project identity not exist");
    }
  }

  @Override
  public ProjectIdentityEntity findOne(Map<String, String> data) {

    if (data.get("id") != null) {
      Long id = Long.parseLong(data.get("id"));
      Optional<ProjectEntity> project = projectRepository.findById(id);
      if (!project.isPresent())
        throw new NotFoundException("project not found");
      Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(id);
      if (!projectIdentity.isPresent())
        throw new NotFoundException("project identity not found");
      return projectIdentity.get();
    }
    throw new BadRequestException("param not exist");

  }

  @Override
  public ProjectIdentityEntity create(CreateDto data) {
    TypologyEntity typology = data.getTypology() != null ? typologyRepository.findById(data.getTypology()).orElse(null)
        : null;
    MinisterEntity minister = data.getMinister() != null ? ministerRepository.findById(data.getMinister()).orElse(null)
        : null;

    SectorEntity sector = data.getSector() != null ? sectorRepository.findById(data.getSector()).orElse(null)
        : null;
    CategoryEntity category = data.getCategory() != null ? categoryRepository.findById(data.getCategory()).orElse(null)
        : null;
    CategoryEnvergureEntity categoryenvergure = data.getEnvergure() != null
        ? categoryEnvergureRepository.findById(data.getEnvergure()).orElse(null)
        : null;
    String generatedCode = generateProjectCode(data, minister);

    ProjectIdentityEntity newProjectIdentityEntity = new ProjectIdentityEntity();

    newProjectIdentityEntity.setCode(generatedCode);
    newProjectIdentityEntity.setName(data.getName());
    newProjectIdentityEntity.setDescription(data.getDescription());
    newProjectIdentityEntity.setTypology(typology);
    newProjectIdentityEntity.setCategory(category);

    newProjectIdentityEntity.setSector(sector);
    newProjectIdentityEntity.setMinister(minister);
    newProjectIdentityEntity.setEnvergure(categoryenvergure);
    newProjectIdentityEntity.setMinisterName(data.getMinisterName());
    newProjectIdentityEntity.setOrganisation(data.getOrganisation());
    newProjectIdentityEntity.setResponsibleName(data.getResponsibleName());
    newProjectIdentityEntity.setResponsibleEmail(data.getResponsibleEmail());
    newProjectIdentityEntity.setResponsiblePhone(data.getResponsiblePhone());
    newProjectIdentityEntity.setManagementUnitName(data.getManagementUnitName());
    newProjectIdentityEntity.setProjectManagerName(data.getProjectManagerName());
    newProjectIdentityEntity.setProjectManagerEmail(data.getProjectManagerEmail());
    newProjectIdentityEntity.setProjectManagerPhone(data.getProjectManagerPhone());
    newProjectIdentityEntity.setProjectOwnerName(data.getProjectOwnerName());
    newProjectIdentityEntity.setProjectOwnerEmail(data.getProjectOwnerEmail());
    newProjectIdentityEntity.setProjectOwnerPhone(data.getProjectOwnerPhone());

    newProjectIdentityEntity.setStatut(ProjectStaut.brouillon);

    return projectIdentityRepository.save(newProjectIdentityEntity);

  }

  @Override
  public ProjectIdentityEntity update(Long id, UpdateDto data) {

    Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(id);
    TypologyEntity typology = data.getTypology() != null ? typologyRepository.findById(data.getTypology()).orElse(null)
        : null;
    MinisterEntity minister = data.getMinister() != null ? ministerRepository.findById(data.getMinister()).orElse(null)
        : null;

    SectorEntity sector = data.getSector() != null ? sectorRepository.findById(data.getSector()).orElse(null)
        : null;
    CategoryEntity category = data.getCategory() != null ? categoryRepository.findById(data.getCategory()).orElse(null)
        : null;
    CategoryEnvergureEntity categoryEnvergure = data.getEnvergure() != null
        ? categoryEnvergureRepository.findById(data.getEnvergure()).orElse(null)
        : null;

    if (projectIdentity.isPresent()) {
      String generatedCode = generateProjectCode(data, minister);
      ProjectIdentityEntity newProjectIdentity = projectIdentity.get();
      newProjectIdentity.setCode(generatedCode);
      newProjectIdentity.setName(data.getName());
      newProjectIdentity.setDescription(data.getDescription());
      newProjectIdentity.setTypology(typology);
      newProjectIdentity.setCategory(category);
      newProjectIdentity.setSector(sector);
      newProjectIdentity.setMinister(minister);
      newProjectIdentity.setMinisterName(data.getMinisterName());
      newProjectIdentity.setOrganisation(data.getOrganisation());
      newProjectIdentity.setResponsibleName(data.getResponsibleName());
      newProjectIdentity.setResponsibleEmail(data.getResponsibleEmail());
      newProjectIdentity.setResponsiblePhone(data.getResponsiblePhone());
      newProjectIdentity.setManagementUnitName(data.getManagementUnitName());
      newProjectIdentity.setProjectManagerName(data.getProjectManagerName());
      newProjectIdentity.setProjectManagerEmail(data.getProjectManagerEmail());
      newProjectIdentity.setProjectManagerPhone(data.getProjectManagerPhone());
      newProjectIdentity.setProjectOwnerName(data.getProjectOwnerName());
      newProjectIdentity.setProjectOwnerEmail(data.getProjectOwnerEmail());
      newProjectIdentity.setProjectOwnerPhone(data.getProjectOwnerPhone());
      newProjectIdentity.setEnvergure(categoryEnvergure);
      return projectIdentityRepository.save(newProjectIdentity);
    } else {
      throw new NotFoundException("project identity not found");
    }

  }

  @Override
  public void delete(Long id) {
    Optional<ProjectIdentityEntity> identity = projectIdentityRepository.findById(id);
    if (identity.isPresent()) {
      projectIdentityRepository.deleteById(id);
    } else {
      throw new NotFoundException("project identity not exist");
    }
  }

  private String generateProjectCode(CreateDto project, MinisterEntity ministre) {
    // Example: First two letters of the project name + minister code + sector code
    // + year + sequential number
    String projectPrefix = "TR";
    String ministerCode = ministre.getCode();
    Date currentDate = new Date();
    String year = String.valueOf(currentDate.getYear() + 1900); // Adjust for Java's Date year offset

    // Generate sequential number (this could be managed based on existing records)
    long projectCount = projectIdentityRepository.count();
    String sequenceNumber = String.format("%04d", projectCount + 1);

    // Final code format: Prefix + MinisterCode + Year + Sequence
    return String.format("%s-%s-%s-%s", projectPrefix, ministerCode, year, sequenceNumber);
  }

  private String generateProjectCode(UpdateDto project, MinisterEntity ministre) {
    // Example: First two letters of the project name + minister code + sector code
    // + year + sequential number
    String projectPrefix = "TR";
    String ministerCode = ministre.getCode();
    String year = String.valueOf(2024);

    // Generate sequential number (this could be managed based on existing records)
    long projectCount = projectIdentityRepository.count();
    String sequenceNumber = String.format("%04d", projectCount + 1);

    // Final code format: Prefix + MinisterCode + Year + Sequence
    return String.format("%s-%s-%s-%s", projectPrefix, ministerCode, year, sequenceNumber);
  }

  public ProjectIdentityEntity ForcevalidateProject(Long id) {
    Optional<ProjectIdentityEntity> projectIdentity = projectIdentityRepository.findById(id);
    if (projectIdentity.isPresent()) {
      ProjectIdentityEntity result = new ProjectIdentityEntity();
      ProjectIdentityEntity newProjectIdentityEntity = projectIdentity.get();
      if (newProjectIdentityEntity.getStatut() == ProjectStaut.brouillon
          || newProjectIdentityEntity.getStatut() == ProjectStaut.finalisée) {
        newProjectIdentityEntity.setStatut(ProjectStaut.validé);
        result = projectIdentityRepository.save(newProjectIdentityEntity);
      }
      return result;
    } else {
      throw new NotFoundException("project identity not found");
    }

  }

  @Transactional
  public ProjectIdentityEntity validateProject(Long id) {
    ProjectIdentityEntity project = projectIdentityRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Project identity not found"));

    // Check completeness before validation
    if (checkProjectCompleteness(id) && project.getStatut() == ProjectStaut.brouillon) {
      project.setStatut(ProjectStaut.finalisée);
      return projectIdentityRepository.save(project);
    } else {
      throw new BadRequestException("Cannot validate incomplete project. Missing sections: "
          + String.join(", ", getMissingComponents(id)));
    }
  }

  @Transactional
  public ProjectIdentityEntity finalvalidateProject(Long id) {
    ProjectIdentityEntity project = projectIdentityRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Project identity not found"));

    // Check completeness before validation
    if (project.getStatut() == ProjectStaut.finalisée) {
      project.setStatut(ProjectStaut.validé);
      return projectIdentityRepository.save(project);
    } else {
      throw new BadRequestException(
          "quelque chose ne va pas avec le projet. Il est peut-être incomplet ou déjà validé.");
    }
  }

  @Override
  @Transactional
  public void blockProject(Long projectId, String reason) {
    ProjectIdentityEntity project = projectIdentityRepository.findById(projectId)
        .orElseThrow(() -> new NotFoundException("Project not found"));

    if (project.getStatut() == ProjectStaut.bloqué) {
      throw new BadRequestException("Project is already blocked.");
    }
    // if (!checkProjectCompleteness(projectId)) {
    // throw new BadRequestException("Cannot block an incomplete project. Missing
    // sections: "
    // + String.join(", ", getMissingComponents(projectId)));
    // }
    if (project.getStatut() == ProjectStaut.validé) {
      throw new BadRequestException("Cannot block a validated project.");
    }
    if (project.getStatut() == ProjectStaut.auto_évalué
        || project.getStatut() == ProjectStaut.auto_évaluation_validée) {

      project.setStatut(ProjectStaut.bloqué);

      ProjectBlockEntity blockRecord = new ProjectBlockEntity();
      blockRecord.setProject(project);
      blockRecord.setReason(reason);
      blockRecord.setBlockedBy("current user");
      blockRecord.setBlockedAt(new Date());

      // Save the entities
      projectBlockRepository.save(blockRecord);
      projectIdentityRepository.save(project);
    } else {
      throw new BadRequestException(
          "quelque chose ne va pas avec le projet. Il est peut-être incomplet ou déjà validé.");
    }
  }

  @Transactional
  public ProjectIdentityEntity unBlockProject(Long id) {
    ProjectBlockEntity block = projectBlockRepository.findByProjectId(id)
        .orElseThrow(() -> new NotFoundException("Project block not found"));
    ProjectIdentityEntity project = projectIdentityRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Project identity not found"));

    // Check completeness before validation
    if (project.getStatut() == ProjectStaut.bloqué) {
      project.setStatut(ProjectStaut.auto_évalué);
      this.projectBlockRepository.delete(block);
      return projectIdentityRepository.save(project);
    } else {
      throw new BadRequestException(
          "quelque chose ne va pas avec le projet. Il est peut-être incomplet ou déjà validé.");
    }
  }

  private boolean checkProjectCompleteness(Long projectId) {
    return projectZoneRepository.existsByProjectIdentityId(projectId) &&
        projectPlanRepository.existsByProjectIdentityId(projectId) &&
        projectStrategyRepository.existsByProjectIdentityId(projectId) &&
        projectLogicRepository.existsByProjectIdentityId(projectId) &&
        projectRiskRepository.existsByProjectIdentityId(projectId);
  }

  private List<String> getMissingComponents(Long projectId) {
    List<String> missingComponents = new ArrayList<>();

    if (!projectZoneRepository.existsByProjectIdentityId(projectId)) {
      missingComponents.add("Zone");
    }
    if (!projectPlanRepository.existsByProjectIdentityId(projectId)) {
      missingComponents.add("Plan");
    }
    if (!projectStrategyRepository.existsByProjectIdentityId(projectId)) {
      missingComponents.add("Strategy");
    }
    if (!projectLogicRepository.existsByProjectIdentityId(projectId)) {
      missingComponents.add("Logic");
    }
    if (!projectRiskRepository.existsByProjectIdentityId(projectId)) {
      missingComponents.add("Risk");
    }

    return missingComponents;
  }

  // You might also want to add a method to check status without changing it
  public boolean isProjectComplete(Long id) {
    return checkProjectCompleteness(id);
  }

  @Override
  @Transactional
  public void archive(Long id) {
    Optional<ProjectIdentityEntity> identity = projectIdentityRepository.findById(id);
    if (identity.isPresent()) {
      ProjectIdentityEntity projectIdentity = identity.get();
      // Instead of changing status, just mark as archived
      projectIdentity.setArchived(true);
      projectIdentityRepository.save(projectIdentity);
    } else {
      throw new NotFoundException("project identity not exist");
    }
  }

  @Override
  @Transactional
  public void unarchive(Long id) {
    Optional<ProjectIdentityEntity> identity = projectIdentityRepository.findById(id);
    if (identity.isPresent()) {
      ProjectIdentityEntity projectIdentity = identity.get();
      projectIdentity.setArchived(false);
      projectIdentityRepository.save(projectIdentity);
    } else {
      throw new NotFoundException("project identity not exist");
    }
  }

  @Override
  public ProjectBlockEntity getBlock(Long id) {
    Optional<ProjectBlockEntity> block = projectBlockRepository.findByProjectId(id);
    if (block.isPresent()) {
      return block.get();
    } else {
      throw new NotFoundException("project block not exist");
    }
  }

  @Override
  @Transactional
  public ProjectBlockEntity updateBlockreason(Long id, String reason) {
    Optional<ProjectBlockEntity> block = projectBlockRepository.findByProjectId(id);
    if (block.isPresent()) {
      ProjectBlockEntity projectBlock = block.get();
      projectBlock.setReason(reason);
      return projectBlockRepository.save(projectBlock);
    } else {
      throw new NotFoundException("project block not exist");
    }

  }

  @Override
  public Long countProjectsByStatus(ProjectStaut status) {
    Long currentUserId = authenticationFacade.getCurrentUserId();
    List<Long> accessibleMinistryIds = getAccessibleMinistryIds(currentUserId);

    List<ProjectIdentityEntity> allProjects = projectIdentityRepository.findAll();

    return allProjects.stream()
        .filter(project -> isProjectAccessible(project, accessibleMinistryIds))
        .filter(project -> project.getStatut() == status)
        .count();
  }

  @Override
  public Long getTotalProjectsBudget() {
    try {

      // Get all project identities
      List<ProjectIdentityEntity> allProjects = projectIdentityRepository.findAll();

      Long totalBudget = 0L;

      for (ProjectIdentityEntity project : allProjects) {
        // Find the project plan for each project
        Optional<ProjectPlanEntity> projectPlanOpt = projectPlanRepository.findByProjectIdentity(project);

        if (projectPlanOpt.isPresent()) {
          ProjectPlanEntity projectPlan = projectPlanOpt.get();
          if (projectPlan.getCoutTotale() != null) {
            totalBudget += projectPlan.getCoutTotale();
          }
        }
      }

      return totalBudget;

    } catch (Exception e) {
      return 0L;
    }
  }

}
