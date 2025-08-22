package com.solidwall.tartib.entities;

import java.util.List;
import java.util.ArrayList;

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
@Table(name = "governorates")
@Entity
public class GovernorateEntity extends AbstractBaseEntity {

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "code", length = 255, nullable = true)
  private String code;

  @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
  private boolean isActive;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false, referencedColumnName = "id")
  private DistrictEntity district;
  @OneToMany(mappedBy = "governorate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<DelegationEntity> delegations = new ArrayList<>();

}
