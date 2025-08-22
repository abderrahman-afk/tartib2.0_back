package com.solidwall.tartib.repositories.abstractions;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import com.solidwall.tartib.dto.roleAccess.FindDto;
import com.solidwall.tartib.entities.RoleAccessEntity;
import com.solidwall.tartib.repositories.interfaces.RoleAccessRepositoryInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class RoleAccessRepositoryAbstract implements RoleAccessRepositoryInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<RoleAccessEntity> findOneByCriteria(FindDto data) {

        System.out.println("--------------------------");
        System.out.println(data.getAccessId());
        System.out.println(data.getAccessName());
        System.out.println(data.getRoleName());
        System.out.println(data.getRoleId());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleAccessEntity> criteriaQuery = criteriaBuilder.createQuery(RoleAccessEntity.class);
        Root<RoleAccessEntity> root = criteriaQuery.from(RoleAccessEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (data.getAccessId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("access").get("id"), data.getAccessId()));
        }
        if (data.getRoleId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("role").get("id"), data.getRoleId()));
        }
        if (data.getAccessName() != null && !data.getAccessName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("access").get("value"), "%" + data.getAccessName() + "%"));
        }
        if (data.getRoleName() != null && !data.getRoleName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("role").get("name"), "%" + data.getRoleName() + "%"));
        }

        // Combine predicates into a single query
        criteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        // Limit the result to one
        return entityManager.createQuery(criteriaQuery).setMaxResults(1).getResultStream().findFirst();
    }
    
}
