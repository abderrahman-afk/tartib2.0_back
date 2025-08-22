package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ActivityEntity;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity,Long>{

}
