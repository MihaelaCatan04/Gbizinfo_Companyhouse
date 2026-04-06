package com.java.companyhouse.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ProcurementDto {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    @Schema(description = "Must reference an existing company corporateNumber in the database")
    private String corporateNumber;
    @NotBlank(message = "mergeKey is required")
    private String mergeKey;
    private OffsetDateTime dateOfOrder;
    private String title;
    private Long amount;
    private String governmentDepartments;
    private String note;
}