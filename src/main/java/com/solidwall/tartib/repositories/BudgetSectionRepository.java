package com.solidwall.tartib.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.BudgetSectionEntity;

@Repository
public interface BudgetSectionRepository extends JpaRepository<BudgetSectionEntity, Long> {

    Optional<BudgetSectionEntity> findByName(String name);

}
