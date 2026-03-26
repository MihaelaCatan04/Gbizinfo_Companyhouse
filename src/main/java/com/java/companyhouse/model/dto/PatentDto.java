package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatentDto {
    private String runId;
    private String corporateNumber;
    private String mergeKey;
    private String patentType;
    private String registrationNumber;
    private String applicationDate;
    private String title;
    private String url;
}