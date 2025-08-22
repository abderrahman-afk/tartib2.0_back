package com.solidwall.tartib.dto.study;

import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
@Getter
@Setter
public class UpdateDto {
    private String name;
    
    private String state;

    private String description;

    private Date realisationDate;
    private boolean isActive;

}
