package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceMajorShareholderSnapshot implements NestedSnapshot<MajorShareholderDto> {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    private String corporateNumber;
    @NotBlank(message = "financeMergeKey is required")
    private String financeMergeKey;
    private List<MajorShareholderDto> shareholders;

    @Override
    public String getParentMergeKey() {
        return financeMergeKey;
    }

    @Override
    public List<MajorShareholderDto> getItems() {
        return shareholders;
    }
}