package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceMajorShareholderSnapshot {
    private String corporateNumber;
    private String financeMergeKey;
    private List<MajorShareholderDto> shareholders;
}