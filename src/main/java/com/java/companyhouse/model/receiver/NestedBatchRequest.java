package com.java.companyhouse.model.receiver;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NestedBatchRequest<S> {
    @Valid
    private List<S> snapshots;
}