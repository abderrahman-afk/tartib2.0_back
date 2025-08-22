package com.solidwall.tartib.entities.base;

import java.io.Serializable;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;
import lombok.Data;
import jakarta.persistence.Id;

/**
 * AbstractBaseEntity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractBaseEntity implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private Date createdAt = new Date();

  @Column(name = "updated_at", nullable = false, updatable = true)
  @LastModifiedDate
  private Date updatedAt = new Date();

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) createdAt = new Date();
    if (this.updatedAt == null) updatedAt = new Date();
  }

  @PreUpdate
  protected void preUpdate() {
    this.updatedAt = new Date();
  }
  
}

