package com.solidwall.tartib.dto.responseadmissibilitygrid;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateDto {
    private Long projectId;
    private Long gridId;
    private List<ResponseCriteriaDto> responses;
    private String derogationText;

}
