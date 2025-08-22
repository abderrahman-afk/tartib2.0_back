package com.solidwall.tartib.entities;

import java.sql.Date;
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
@Table(name = "projects")
@Entity
public class ProjectEntity extends AbstractBaseEntity {

    @Column(name = "simep_code", length = 255, nullable = true, columnDefinition = "TEXT")
    private String simepCode;
    
    @Column(name = "simep_date", nullable = true)
    private Date simepDate;
    
    @Column(name = "observation", length = 255, nullable = true, columnDefinition = "TEXT")
    private String observation;
    
    @Column(name = "status", nullable = true, columnDefinition = "boolean")
    private boolean status;
    
    @Column(name = "is_active", nullable = true, columnDefinition = "boolean")
    private boolean isActive;

}
