package com.java.companyhouse.model.solved;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class SnapshotGroups<S> {
    private List<S> empty;
    private List<S> nonEmpty;
}