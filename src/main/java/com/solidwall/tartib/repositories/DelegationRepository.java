package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.entities.GovernorateEntity;

@Repository
public interface DelegationRepository extends JpaRepository<DelegationEntity, Long> {

     Optional<DelegationEntity> findByName(String name);
     List<DelegationEntity> findByGovernorate(GovernorateEntity governorate);
    List<DelegationEntity> findByGovernorateIdIn(List<Long> governorateIds);

}
