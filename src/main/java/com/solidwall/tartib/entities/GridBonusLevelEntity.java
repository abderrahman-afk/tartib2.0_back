package com.solidwall.tartib.entities;

import com.solidwall.tartib.entities.base.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grid_bonus_level")
@Getter @Setter @NoArgsConstructor
public class GridBonusLevelEntity extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_bonus_id", nullable = false)
    private GridBonusEntity gridBonus;

    @Column(name = "level_name", nullable = false)
    private String levelName; // A, B, C, D, E

    @Column(name = "points_threshold", nullable = false)
    private Double pointsThreshold;

    @Column(name = "bonus_percentage", nullable = false)
    private Double bonusPercentage;

    @Column(name = "comment")
    private String comment;
}
