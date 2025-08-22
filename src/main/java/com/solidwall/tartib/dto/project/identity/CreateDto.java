package com.solidwall.tartib.dto.project.identity;

 
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDto {


  private String code;

  private String name;

  private String description;

  private Long typology;

  private Long category;

  private Long sector;

  private Long minister;

  private String ministerName;

  private String organisation;

  private String responsibleName;

  private String responsibleEmail;

  private String responsiblePhone;

  private String managementUnitName;

  private String projectManagerName;

  private String projectManagerEmail;

  private String projectManagerPhone;

  private String projectOwnerName;

  private String projectOwnerEmail;

  private String projectOwnerPhone;
  
  private Long envergure;
  

}
