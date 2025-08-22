package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "geographic_bonus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeographicBonusEntity extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classification_id", nullable = false)
    private ClassificationEntity classification;
    
    @ManyToOne
    @JoinColumn(name = "geographic_category_id", nullable = false)
    private GeographicCategoryEntity geographicCategory;
    
    @Column(name = "bonus_percentage", nullable = false)
    private Double bonusPercentage;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
}
