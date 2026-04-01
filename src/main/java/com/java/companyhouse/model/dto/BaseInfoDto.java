package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseInfoDto {
    private String corporateNumber;
    private String mergeKey;
    private String averageContinuousServiceYearsType;
    private Double averageContinuousServiceYearsMale;
    private Double averageContinuousServiceYearsFemale;
    private Double averageContinuousServiceYears;
    private Double averageAge;
    private Double monthAveragePredeterminedOvertimeHours;
}