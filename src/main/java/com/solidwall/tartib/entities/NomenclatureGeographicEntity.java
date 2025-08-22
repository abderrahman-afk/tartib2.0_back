package com.solidwall.tartib.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nomenclature_geographic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NomenclatureGeographicEntity extends AbstractBaseEntity {

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "justification_path", length = 1000)
    private String justificationPath;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @OneToMany(mappedBy = "nomenclature",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GeographicCategoryEntity> geographicCategories = new ArrayList<>();
}