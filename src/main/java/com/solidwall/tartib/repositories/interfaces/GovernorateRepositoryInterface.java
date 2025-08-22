package com.solidwall.tartib.repositories.interfaces;

import java.util.List;

import com.solidwall.tartib.entities.GovernorateEntity;

public interface GovernorateRepositoryInterface {

  List<GovernorateEntity> findAllByCriteria(Long district, String param, Boolean active);

}


