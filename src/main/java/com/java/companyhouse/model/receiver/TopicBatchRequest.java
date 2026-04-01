package com.java.companyhouse.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TopicBatchRequest<T> {
    private List<CompanySnapshot<T>> companies;
}
