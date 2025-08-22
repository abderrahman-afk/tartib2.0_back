package com.solidwall.tartib.implementations;

import java.util.List;

import com.solidwall.tartib.entities.OrganisationEntity;

public interface OrganisationImplementation {

    List<OrganisationEntity> findAll();

    OrganisationEntity findOne();

    OrganisationEntity getOne(Long id);

    OrganisationEntity create(OrganisationEntity data);

    OrganisationEntity update(Long id, OrganisationEntity data);

    void delete(Long id);
}
