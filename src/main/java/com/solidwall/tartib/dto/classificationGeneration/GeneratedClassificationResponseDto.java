package com.solidwall.tartib.dto.classificationGeneration;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GeneratedClassificationResponseDto {
    private Long id;
    private String name;
    private Date generationDate;
    private String description;
    private Long classificationSystemId;
    private String classificationSystemTitle;
    private List<ProjectClassementResponseDto> projectClassements;
    private Date createdAt;
    private Date updatedAt;
}
