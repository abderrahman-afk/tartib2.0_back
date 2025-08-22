package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "project_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBlockEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectIdentityEntity project;
    
    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;
    
    @Column(name = "blocked_by", nullable = false)
    private String blockedBy;
    
    @Column(name = "blocked_at", nullable = false)
    private Date blockedAt;
}