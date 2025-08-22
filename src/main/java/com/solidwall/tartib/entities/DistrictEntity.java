package com.solidwall.tartib.entities;



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
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
@Entity
public class DistrictEntity extends AbstractBaseEntity {

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "code", length = 255, nullable = true)
  private String code;

  @Column(name = "description", length = 255, nullable = true, columnDefinition = "TEXT")
  private String description;

  @Column(name = "isActive", nullable = true, columnDefinition = "boolean")
  private boolean isActive;

   @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GovernorateEntity> governorates = new ArrayList<>();

}
