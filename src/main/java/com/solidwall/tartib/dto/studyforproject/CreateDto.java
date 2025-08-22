package com.solidwall.tartib.dto.studyforproject;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CreateDto {
    private String name;

    private String state;

    private String description;

    private Date realisationDate;
}
