package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompatibilityMapper {
    void upsertCompatibility(CompatibilityOfChildcareAndWorkDto dto);

    Long findCompatibilityIdByMergeKey(String mergeKey);
}