package com.solidwall.tartib.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "generated_classification" ,uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedClassificationEntity extends AbstractBaseEntity {

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "generation_date", nullable = false)
    private Date generationDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "classification_system_id", nullable = false)
    private ClassificationEntity classificationSystem;

    @OneToMany(mappedBy = "generatedClassification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectClassementEntity> projectClassements = new ArrayList<>();
}