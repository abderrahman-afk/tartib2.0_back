package com.solidwall.tartib.entities;

import java.util.HashSet;
import java.util.Set;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classification")
@Getter
@Setter
@NoArgsConstructor
public class ClassificationEntity extends AbstractBaseEntity {
    @Column(name = "code", length = 50, nullable = false)
    private String code;
    
    @Column(name = "title", length = 255, nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

        
    @Column(name = "annee", columnDefinition = "TEXT")
    private String year;
    
    @Column(name = "justification_path", length = 1000)
    private String justificationPath;
    
    @Column(name = "active", nullable = false)
    private Boolean active = false;
    
    @ManyToOne
    @JoinColumn(name = "nomenclature_secteur_id", nullable = false)
    private NomenclatureSecteurEntity nomenclatureSecteur;
    
    @ManyToOne
    @JoinColumn(name = "nomenclature_geographic_id", nullable = false)
    private NomenclatureGeographicEntity nomenclatureGeographic;
    
    @ManyToOne
    @JoinColumn(name = "evaluation_grid_id", nullable = false)
    private EvaluationGridEntity evaluationGrid;
    
    @OneToMany(mappedBy = "classification", cascade = CascadeType.ALL,  fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SecteurBonusEntity> secteurBonuses = new HashSet<>();
    
    @OneToMany(mappedBy = "classification", cascade = CascadeType.ALL,   fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<GeographicBonusEntity> geographicBonuses = new HashSet<>();

    @OneToMany(mappedBy = "classification", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
private Set<GridBonusEntity> gridBonuses = new HashSet<>();
    
}