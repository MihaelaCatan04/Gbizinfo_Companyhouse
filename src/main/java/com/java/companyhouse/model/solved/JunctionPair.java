package com.java.companyhouse.model.solved;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class JunctionPair {
    private long companyId;
    private long childId;
}
