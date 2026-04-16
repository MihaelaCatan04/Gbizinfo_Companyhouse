package com.java.companyhouse.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassificationDto {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    @Schema(description = "Must reference an existing company corporateNumber in the database")
    private String corporateNumber;
    @Schema(description = "Must reference an existing patent mergeKey in the database")
    @NotBlank(message = "patentMergeKey is required")
    private String patentMergeKey;
    private String mergeKey;
    private String codeValue;
    private String codeName;
    private String japanese;
}