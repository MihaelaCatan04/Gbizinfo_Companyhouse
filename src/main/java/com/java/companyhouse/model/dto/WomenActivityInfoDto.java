package com.java.companyhouse.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WomenActivityInfoDto {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    @Schema(description = "Must reference an existing company corporateNumber in the database")
    private String corporateNumber;
    @NotBlank(message = "mergeKey is required")
    private String mergeKey;
    private String femaleWorkersProportionType;
    private Double femaleWorkersProportion;
    private Integer femaleShareOfManager;
    private Integer genderTotalOfManager;
    private Integer femaleShareOfOfficers;
    private Integer genderTotalOfOfficers;
}