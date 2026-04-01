package com.java.companyhouse.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MajorShareholderBatchRequest {
    private List<FinanceMajorShareholderSnapshot> finances;
}