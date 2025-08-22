package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.ComponentEntity;

@Repository
public interface ComponentRepository extends JpaRepository<ComponentEntity, Long>{

}
