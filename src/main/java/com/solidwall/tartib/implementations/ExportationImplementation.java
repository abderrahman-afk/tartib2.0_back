package com.solidwall.tartib.implementations;

import com.solidwall.tartib.dto.exportation.AdminEvaluationScoresToExportDto;
import com.solidwall.tartib.dto.exportation.ClassificationResultsExportDto;
import com.solidwall.tartib.dto.exportation.ProjectDetailsToExportDto;
import com.solidwall.tartib.dto.exportation.ProjectToExportDto;
import com.solidwall.tartib.dto.exportation.ClassificationResultsExportDto.ProjectResultExportDto;
import com.solidwall.tartib.entities.ProjectClassementEntity;
import com.solidwall.tartib.entities.ProjectIdentityEntity;
import java.util.List;

public interface ExportationImplementation {
public List<ProjectDetailsToExportDto> exportAllProjectsDetails(Integer year);
public ProjectToExportDto exportProjectData(long projectId) ;
public AdminEvaluationScoresToExportDto exportAdminEvaluationScores(long projectId);
public ClassificationResultsExportDto exportClassificationResults();

public ProjectResultExportDto mapProjectClassementToExport(ProjectClassementEntity projectClassement);
public String getProjectGovernorates(ProjectIdentityEntity project);
public Long getProjectTotalCost(ProjectIdentityEntity project);
}
