package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_study")
@Entity
public class ProjectStudyEntity extends AbstractBaseEntity {

   @OneToOne
    @JoinColumn(name = "project_identity_id", nullable = true, unique = true)
    private ProjectIdentityEntity projectIdentity;

    @OneToMany(mappedBy = "projectStudy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AutorisationEntity> autorisations = new ArrayList<>();

    @OneToMany(mappedBy = "projectStudy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudyForProject> studies = new ArrayList<>();

}
