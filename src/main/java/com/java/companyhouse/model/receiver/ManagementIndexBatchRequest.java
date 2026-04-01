package com.java.companyhouse.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagementIndexBatchRequest {
    private List<FinanceManagementIndexSnapshot> finances;
}
