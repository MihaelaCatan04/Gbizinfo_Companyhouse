package com.java.companyhouse.model.receiver;

import java.util.List;

public interface NestedSnapshot<T> {
    String getCorporateNumber();

    String getParentMergeKey();

    List<T> getItems();
}
