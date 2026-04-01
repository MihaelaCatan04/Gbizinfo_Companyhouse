package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceManagementIndexSnapshot {
    private String financeMergeKey;
    private List<ManagementIndexDto> managementIndexes;
}
