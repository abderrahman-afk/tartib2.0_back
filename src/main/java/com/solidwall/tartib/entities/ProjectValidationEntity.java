package com.solidwall.tartib.entities;

import java.sql.Date;
import com.solidwall.tartib.entities.base.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_validation")
@Entity
public class ProjectValidationEntity extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private ProjectEntity project;

    @Column(name = "additional_information", length = 255, nullable = true)
    private String additionalInformation;

    @Column(name = "document_name", length = 255, nullable = true)
    private String documentName;

    @Column(name = "author", length = 255, nullable = true)
    private String author;

    @Column(name = "submission_date", nullable = true)
    private Date submissionDate;

}
