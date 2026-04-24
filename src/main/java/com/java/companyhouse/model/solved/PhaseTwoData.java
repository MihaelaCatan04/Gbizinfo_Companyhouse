package com.java.companyhouse.model.solved;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PhaseTwoData<R, P> {
    private List<R> relations;
    private List<P> processed;
}