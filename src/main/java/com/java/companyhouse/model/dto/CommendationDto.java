package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommendationDto {
    private String runId;
    private String corporateNumber;
    private String mergeKey;
    private String dateOfCommendation;
    private String title;
    private String target;
    private String category;
    private String governmentDepartments;
    private String note;
}