package com.solidwall.tartib.dto.projectevaluation.request;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateComponentEvaluationDto {
    private Long componentId;
    private List<CreateCriteriaEvaluationDto> criteria;
}
