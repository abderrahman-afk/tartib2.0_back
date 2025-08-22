package com.solidwall.tartib.dto.project.validation;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

  private Long project;

  private String additionalInformation;

  private String documentName;

  private String author;

  private Date submissionDate;

}
