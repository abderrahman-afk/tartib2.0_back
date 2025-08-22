package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.FoundException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.OrganisationEntity;
import com.solidwall.tartib.implementations.OrganisationImplementation;
import com.solidwall.tartib.repositories.OrganisationRepository;

@Service
public class OrganisationService implements OrganisationImplementation {

    @Autowired
    OrganisationRepository organisationRepository;

    @Override
    public OrganisationEntity getOne(Long id) {
        Optional<OrganisationEntity> data = organisationRepository.findById(id);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new NotFoundException("organisation not exist");
        }
    }

    @Override
    public OrganisationEntity findOne() {
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public List<OrganisationEntity> findAll() {
        if (!organisationRepository.findAll().isEmpty()) {
            return organisationRepository.findAll();
        } else {
            throw new NotFoundException("not exist any organisation ");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<OrganisationEntity> data = organisationRepository.findById(id);
        if (data.isPresent()) {
            organisationRepository.deleteById(id);
        } else {
            throw new NotFoundException("organisation not exist");
        }
    }

    @Override
    public OrganisationEntity create(OrganisationEntity data) {
        Optional<OrganisationEntity> organisation = organisationRepository.findByName(data.getName());
        if (!organisation.isPresent()) {
            return organisationRepository.save(data);
        } else {
            throw new FoundException("organisation already exist");
        }
    }

    @Override
    public OrganisationEntity update(Long id, OrganisationEntity data) {
        Optional<OrganisationEntity> organism = organisationRepository.findById(id);
        if (organism.isPresent()) {
            OrganisationEntity updatedOrganisation = organism.get();
            updatedOrganisation.setName(data.getName());
            updatedOrganisation.setCode(data.getCode());
            updatedOrganisation.setDescription(data.getDescription());
            updatedOrganisation.setActive(data.isActive());
            updatedOrganisation.setUpdatedAt(data.getUpdatedAt());
            return organisationRepository.save(updatedOrganisation);
        } else {
            throw new NotFoundException("organisation not found");
        }
    }
}
