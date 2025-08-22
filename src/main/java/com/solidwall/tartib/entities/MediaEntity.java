package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
@Entity
public class MediaEntity extends AbstractBaseEntity {

  @Column(name = "extention", length = 255, nullable = true)
  private String extention;

  @Column(name = "size", length = 255, nullable = true)
  private float size;

  @Column(name = "type", length = 255, nullable = true)
  private String type;

  @Column(name = "source", length = 255, nullable = false, columnDefinition = "TEXT")
  private String source;

}
