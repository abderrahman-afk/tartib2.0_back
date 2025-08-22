package com.solidwall.tartib.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import java.util.List;


@Repository
public interface GovernorateRepository extends JpaRepository<GovernorateEntity, Long> {

  Optional<GovernorateEntity> findByName(String name);
  List<GovernorateEntity> findByDistrict(DistrictEntity district);
  List<GovernorateEntity> findByDistrictIdIn(List<Long> districtIds);


}
