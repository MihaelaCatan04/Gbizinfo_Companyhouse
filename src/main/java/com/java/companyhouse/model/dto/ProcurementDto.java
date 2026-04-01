package com.java.companyhouse.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcurementDto {
    private String corporateNumber;
    private String mergeKey;
    private String dateOfOrder;
    private String title;
    private Long amount;
    private String governmentDepartments;
    private String note;
}