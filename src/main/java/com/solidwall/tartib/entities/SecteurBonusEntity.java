package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "secteur_bonus")
@Getter
@Setter
@NoArgsConstructor
public class SecteurBonusEntity extends AbstractBaseEntity {
    @ManyToOne
    @JoinColumn(name = "classification_id", nullable = false)
    private ClassificationEntity classification;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "secteur_id", nullable = false)
    private SecteurEntity secteur;
    
    @Column(name = "bonus_percentage", nullable = false)
    private Double bonusPercentage;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
}