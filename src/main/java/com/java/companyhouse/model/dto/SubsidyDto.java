package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubsidyDto {
    private String corporateNumber;
    private String mergeKey;
    private String dateOfApproval;
    private String title;
    private String amount;
    private String target;
    private String governmentDepartments;
}