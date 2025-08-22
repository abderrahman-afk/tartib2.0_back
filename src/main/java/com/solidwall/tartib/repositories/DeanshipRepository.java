package com.solidwall.tartib.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.DeanshipEntity;
import com.solidwall.tartib.entities.DelegationEntity;

@Repository
public interface DeanshipRepository extends JpaRepository<DeanshipEntity, Long> {

     Optional<DeanshipEntity> findByName(String name);
     List<DeanshipEntity> findByDelegation(DelegationEntity delegation);
    List<DeanshipEntity> findByDelegationIdIn(List<Long> delegationIds);

}
