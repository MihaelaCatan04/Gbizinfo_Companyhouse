package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceManagementIndexSnapshot implements NestedSnapshot<ManagementIndexDto> {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    private String corporateNumber;
    @NotBlank(message = "financeMergeKey is required")
    @Valid
    private String financeMergeKey;
    @Valid
    private List<ManagementIndexDto> managementIndexes;

    @Override
    public String getParentMergeKey() {
        return financeMergeKey;
    }

    @Override
    public List<ManagementIndexDto> getItems() {
        return managementIndexes;
    }
}