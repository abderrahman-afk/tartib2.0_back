package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.funding.tranche.CreateDto;
import com.solidwall.tartib.dto.funding.tranche.UpdateDto;
import com.solidwall.tartib.entities.FundingTrancheEntity;
import com.solidwall.tartib.entities.ProjectPlanEntity;
import com.solidwall.tartib.implementations.FundingTrancheImplementation;
import com.solidwall.tartib.repositories.FundingTrancheRepository;
import com.solidwall.tartib.repositories.ProjectPlanRepository;

@Service
public class FundingTrancheService implements FundingTrancheImplementation {

    @Autowired
    FundingTrancheRepository fundingTrancheRepository;

    @Autowired
    ProjectPlanRepository projectPlanRepository;

    @Override
    public FundingTrancheEntity getOne(Long id) {
        Optional<FundingTrancheEntity> fundingTranche = fundingTrancheRepository.findById(id);
        if (fundingTranche.isPresent()) {
            return fundingTranche.get();
        } else {
            throw new NotFoundException("Funding Tranches not exist");
        }
    }

    @Override
    public FundingTrancheEntity findOne(Map<String, String> data) {
        throw new BadRequestException("param not exist");

    }

    @Override
    public List<FundingTrancheEntity> findAll() {
        if (!fundingTrancheRepository.findAll().isEmpty()) {
            return fundingTrancheRepository.findAll();
        } else {
            throw new NotFoundException("not exist any funding tranche");
        }
    }

    @Override
    public FundingTrancheEntity create(CreateDto data) {
        ProjectPlanEntity projectPlan = data.getProjectPlan() != null
                ? projectPlanRepository.findById(data.getProjectPlan()).orElse(null)
                : null;
        FundingTrancheEntity newFundingTranche = new FundingTrancheEntity();
        newFundingTranche.setAmount(data.getAmount());
        newFundingTranche.setDescription(data.getDescription());
        newFundingTranche.setDisbursement(data.getDisbursement());
        newFundingTranche.setProjectPlan(projectPlan);
        newFundingTranche.setTitle(data.getTitle());
        newFundingTranche.setYear(data.getYear());
        return fundingTrancheRepository.save(newFundingTranche);
    }

    @Override
    public FundingTrancheEntity update(Long id, UpdateDto data) {

        Optional<FundingTrancheEntity> fundingTranche = fundingTrancheRepository.findById(id);
        ProjectPlanEntity projectPlan = data.getProjectPlan() != null
                ? projectPlanRepository.findById(data.getProjectPlan()).orElse(null)
                : null;
        if (fundingTranche.isPresent()) {
            FundingTrancheEntity updateFundingTranche = fundingTranche.get();
            updateFundingTranche.setAmount(data.getAmount());
            updateFundingTranche.setDescription(data.getDescription());
            updateFundingTranche.setDisbursement(data.getDisbursement());
            updateFundingTranche.setProjectPlan(projectPlan);
            updateFundingTranche.setTitle(data.getTitle());
            updateFundingTranche.setYear(data.getYear());
            return fundingTrancheRepository.save(updateFundingTranche);
        } else {
            throw new NotFoundException("funding tranche not found");
        }
    }

    @Override
    public void delete(Long id) {
        Optional<FundingTrancheEntity> fundingTranche = fundingTrancheRepository.findById(id);
        if (fundingTranche.isPresent()) {
            fundingTrancheRepository.deleteById(id);
        } else {
            throw new NotFoundException("funding tranche not exist");
        }
    }

}
