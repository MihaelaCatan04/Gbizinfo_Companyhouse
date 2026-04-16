package com.java.companyhouse.model.receiver;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface NestedSnapshot<T> {
    String getCorporateNumber();

    @JsonIgnore
    String getParentMergeKey();

    @JsonIgnore
    List<T> getItems();
}
