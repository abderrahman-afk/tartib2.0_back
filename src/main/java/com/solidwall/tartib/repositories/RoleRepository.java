package com.solidwall.tartib.repositories;

import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
