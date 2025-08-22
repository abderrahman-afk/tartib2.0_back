package com.solidwall.tartib.dto.project;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {

    private String simepCode;

    private Date simepDate;

    private String observation;

    private boolean status;

    private boolean isActive;

}
