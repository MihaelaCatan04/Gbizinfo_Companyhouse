package com.java.companyhouse.model.receiver;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicBatchRequest<T> {
    @Valid
    private List<CompanySnapshot<T>> companies;
}
