package com.solidwall.tartib.repositories.abstractions;

import java.util.List;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

import com.solidwall.tartib.entities.GovernorateEntity;
import com.solidwall.tartib.repositories.interfaces.GovernorateRepositoryInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class GovernorateRepositoryAbstract implements GovernorateRepositoryInterface {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<GovernorateEntity> findAllByCriteria(Long district, String param, Boolean active) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<GovernorateEntity> criteriaQuery = criteriaBuilder.createQuery(GovernorateEntity.class);
    Root<GovernorateEntity> governorate = criteriaQuery.from(GovernorateEntity.class);

    List<Predicate> predicates = new ArrayList<>();

    if (param != null && !param.isEmpty()) {
      Predicate namePredicate = criteriaBuilder.equal(governorate.get("name"), param);
      Predicate codePredicate = criteriaBuilder.equal(governorate.get("code"), param);
      Predicate paramPredicate = criteriaBuilder.or(namePredicate, codePredicate);
      predicates.add(paramPredicate);
    }

    if (district != null) {
      predicates.add(criteriaBuilder.equal(governorate.get("district").get("id"), district));
    }

    if (active != null) {
      predicates.add(criteriaBuilder.equal(governorate.get("active"), active));
    }

    return entityManager.createQuery(criteriaQuery).getResultList();
  }

}
