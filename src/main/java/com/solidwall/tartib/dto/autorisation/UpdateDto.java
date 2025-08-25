package com.solidwall.tartib.dto.autorisation;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {

    private String name;
    private String validateur;
    private String observation;
    private MultipartFile justificationFile;

}
