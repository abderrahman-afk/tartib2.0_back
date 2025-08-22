package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solidwall.tartib.entities.ProjectIdentityEntity;
import com.solidwall.tartib.entities.ProjectScaleEntity;
import java.util.List;


public interface ProjectScaleRepository extends JpaRepository<ProjectScaleEntity, Long> {

    @Query("SELECT pr FROM ProjectScaleEntity pr WHERE pr.minimumBudget <=:max AND pr.maximumBudget >=:min")
    List<ProjectScaleEntity> findByMaximumBudgetAndMinimumBudget(Long min, Long max);
    ProjectScaleEntity findByMinimumBudgetLessThanEqualAndMaximumBudgetGreaterThanEqual(Long budget,Long budget2);
     List<ProjectScaleEntity> findByProjectIdentity(ProjectIdentityEntity projectIdentity); 

 }
