package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WomenActivityInfoDto {
    private String corporateNumber;
    private String mergeKey;
    private String femaleWorkersProportionType;
    private Double femaleWorkersProportion;
    private Integer femaleShareOfManager;
    private Integer genderTotalOfManager;
    private Integer femaleShareOfOfficers;
    private Integer genderTotalOfOfficers;
}