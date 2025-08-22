package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "project_strategy")
@Entity
public class ProjectStrategyEntity extends AbstractBaseEntity {

   @OneToOne
    @JoinColumn(name = "project_identity_id", nullable = true, unique = true)
    private ProjectIdentityEntity projectIdentity;

    @ManyToOne
    @JoinColumn(name = "pnd_id", nullable = true)
    private PndEntity pnd;

    @ManyToOne
    @JoinColumn(name = "pnd_axis_id", nullable = true)
    private PndAxisEntity pndAxis;

    @Column(name = "politique_name", length = 255, nullable = true)
    private String politicalName;

    @Column(name = "politique_axe", length = 255, nullable = true)
    private String politicalAxe;
     
    @Column(name = "objective", nullable = true, columnDefinition = "TEXT")
    private String objective;

    @Column(name = "terrority", nullable = true, columnDefinition = "TEXT")
    private String terrority;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;
 
    @Column(name = "local_pnd", nullable = true, columnDefinition = "TEXT")
    private String pndLocal;
    
    @Column(name = "reference", nullable = true, columnDefinition = "TEXT")
    private String reference;
 

}
