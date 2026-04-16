package com.java.companyhouse.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagementIndexDto {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    @Schema(description = "Must reference an existing company corporateNumber in the database")
    private String corporateNumber;
    @NotBlank(message = "financeMergeKey is required")
    @Schema(description = "Must reference an existing finance mergeKey in the database")
    private String financeMergeKey;
    @NotBlank(message = "mergeKey is required")
    private String mergeKey;
    private int period;
    private Long netSalesSummaryOfBusinessResults;
    private String netSalesSummaryOfBusinessResultsUnitRef;
    private Long operatingRevenue1SummaryOfBusinessResults;
    private String operatingRevenue1SummaryOfBusinessResultsUnitRef;
    private Long operatingRevenue2SummaryOfBusinessResults;
    private String operatingRevenue2SummaryOfBusinessResultsUnitRef;
    private Long grossOperatingRevenueSummaryOfBusinessResults;
    private String grossOperatingRevenueSummaryOfBusinessResultsUnitRef;
    private Long ordinaryIncomeSummaryOfBusinessResults;
    private String ordinaryIncomeSummaryOfBusinessResultsUnitRef;
    private Long netPremiumsWrittenSummaryOfBusinessResultIns;
    private String netPremiumsWrittenSummaryOfBusinessResultsInsUnitRef;
    private Long ordinaryIncomeLossSummaryOfBusinessResults;
    private String ordinaryIncomeLossSummaryOfBusinessResultsUnitRef;
    private Long netIncomeLossSummaryOfBusinessResults;
    private String netIncomeLossSummaryOfBusinessResultsUnitRef;
    private Long capitalStockSummaryOfBusinessResults;
    private String capitalStockSummaryOfBusinessResultsUnitRef;
    private Long netAssetsSummaryOfBusinessResults;
    private String netAssetsSummaryOfBusinessResultsUnitRef;
    private Long totalAssetsSummaryOfBusinessResults;
    private String totalAssetsSummaryOfBusinessResultsUnitRef;
    private Long numberOfEmployees;
    private String numberOfEmployeesUnitRef;
}