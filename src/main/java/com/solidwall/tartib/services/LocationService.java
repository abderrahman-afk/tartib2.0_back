package com.solidwall.tartib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.entities.DeanshipEntity;
import com.solidwall.tartib.entities.DelegationEntity;
import com.solidwall.tartib.entities.DistrictEntity;
import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.repositories.DeanshipRepository;
import com.solidwall.tartib.repositories.DelegationRepository;
import com.solidwall.tartib.repositories.DistrictRepository;
import com.solidwall.tartib.repositories.GovernorateRepository;

@Service
public class LocationService {
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private GovernorateRepository governorateRepository;
    @Autowired
    private DelegationRepository delegationRepository;
    @Autowired
    private DeanshipRepository deanshipRepository;

    public List<DistrictEntity> getAllDistricts() {
        return districtRepository.findAll();
    }

    public List<GovernorateEntity> getGovernoratesByDistricts(List<Long> districtIds) {
        return governorateRepository.findByDistrictIdIn(districtIds);
    }

    public List<DelegationEntity> getDelegationsByGovernorates(List<Long> governorateIds) {
        return delegationRepository.findByGovernorateIdIn(governorateIds);
    }

    public List<DeanshipEntity> getDeanshipsByDelegations(List<Long> delegationIds) {
        return deanshipRepository.findByDelegationIdIn(delegationIds);
    }
}