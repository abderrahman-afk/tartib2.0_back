package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.solidwall.tartib.entities.AccessEntity;

public interface AccessRepository extends  JpaRepository<AccessEntity, Long> {


}
