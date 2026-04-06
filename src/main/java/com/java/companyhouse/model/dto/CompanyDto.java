package com.java.companyhouse.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompanyDto {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    private String corporateNumber;
    private String name;
    private String kana;
    private String nameEn;
    private String postalCode;
    private String location;
    private String process;
    private String aggregatedYear;
    private String status;
    private LocalDate closeDate;
    private String closeCause;
    private String kind;
    private String representativeName;
    private Long capitalStock;
    private Integer employeeNumber;
    private Integer companySizeMale;
    private Integer companySizeFemale;
    private String businessSummary;
    private String companyUrl;
    private Integer foundingYear;
    private LocalDate dateOfEstablishment;
    private String qualificationGrade;
    private String updateDate;
    private String workplaceInfoMergeKey;
}