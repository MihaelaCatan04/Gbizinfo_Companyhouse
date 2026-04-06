package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MajorShareholderDto {
    private String corporateNumber;
    private String financeMergeKey;
    private String mergeKey;
    private String nameMajorShareholders;
    private Double shareholdingRatio;
}