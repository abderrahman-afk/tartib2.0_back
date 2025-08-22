package com.solidwall.tartib.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "geo_category")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GeographicCategoryEntity extends AbstractBaseEntity {
    
    @Column(name = "code", length = 50, nullable = false)
    private String code;
    
    @Column(name = "title", length = 255, nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne()
    @JoinColumn(name = "nomenclature_id", nullable = false)
    @JsonBackReference
    private NomenclatureGeographicEntity nomenclature;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "category_governorate",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "governorate_id")
    )
    private Set<GovernorateEntity> governorates = new HashSet<>();
}