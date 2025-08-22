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
@Table(name = "secteur")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SecteurEntity extends AbstractBaseEntity {
    
    @Column(name = "code", length = 50, nullable = false)
    private String code;
    
    @Column(name = "title", length = 255, nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne()
    @JoinColumn(name = "nomenclature_id", nullable = false)
    @JsonBackReference
    private NomenclatureSecteurEntity nomenclature;
    
    @ManyToMany(fetch = FetchType.EAGER)  // Make ministers EAGER too
    @JoinTable(
        name = "secteur_minister",
        joinColumns = @JoinColumn(name = "secteur_id"),
        inverseJoinColumns = @JoinColumn(name = "minister_id")
    )
    private Set<MinisterEntity> ministers = new HashSet<>();
}