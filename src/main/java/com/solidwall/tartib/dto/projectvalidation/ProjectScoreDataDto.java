package com.solidwall.tartib.dto.projectvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectScoreDataDto {
    private double[] projectScores;
    private double[] averageScores;
    private double[] averageScoresByMinistry;  // New field for ministry averages
    private String[] componentNames;
}