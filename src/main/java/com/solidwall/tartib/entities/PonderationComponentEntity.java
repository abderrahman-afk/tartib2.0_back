package com.solidwall.tartib.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ponderation_component")
public class PonderationComponentEntity extends AbstractBaseEntity {
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "number_of_points", nullable = true ,  scale = 2)
    private Double numberOfPoints;
    
    @Column(name = "percentage", nullable = true ,  scale = 2)
    private Double percentage;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponderation_id", nullable = true)
    private PonderationEntity ponderation;
    
    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    private List<PonderationCriteriaEntity> ponderationcriterias;
}
