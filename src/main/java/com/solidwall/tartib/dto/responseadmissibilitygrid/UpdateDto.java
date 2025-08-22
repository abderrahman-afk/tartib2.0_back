package com.solidwall.tartib.dto.responseadmissibilitygrid;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateDto {
    private List<ResponseCriteriaDto> responses;
    private String derogationText;
    private String status;
    private boolean isAdmissible;
}
