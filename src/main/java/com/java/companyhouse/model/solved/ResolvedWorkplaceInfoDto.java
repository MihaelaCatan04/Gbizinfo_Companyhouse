package com.java.companyhouse.model.solved;

import lombok.Getter;
import lombok.Setter;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;

@Getter
@Setter
public class ResolvedWorkplaceInfoDto {
    private final String mergeKey;
    private final String corporateNumber;
    private final Long baseInfoId;
    private final Long womenActivityInfoId;
    private final Long compatibilityId;

    public ResolvedWorkplaceInfoDto(WorkplaceInfoDto e, Long baseInfoId, Long womenActivityInfoId, Long compatibilityId) {
        this.mergeKey = e.getMergeKey();
        this.corporateNumber = e.getCorporateNumber();
        this.baseInfoId = baseInfoId;
        this.womenActivityInfoId = womenActivityInfoId;
        this.compatibilityId = compatibilityId;
    }
}