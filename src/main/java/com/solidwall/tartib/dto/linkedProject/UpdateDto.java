package com.solidwall.tartib.dto.linkedProject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
        
    private String projectCode;

    private String description;
}
