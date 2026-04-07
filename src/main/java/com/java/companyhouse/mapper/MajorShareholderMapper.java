package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MajorShareholderMapper {

    void upsertMajorShareholder(MajorShareholderDto dto);

    void upsertFinanceShareholder(@Param("mergeKey") String mergeKey, @Param("financeMergeKey") String financeMergeKey, @Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);

    void softDeleteAllFinanceShareholders(@Param("corporateNumber") String corporateNumber, @Param("financeMergeKey") String financeMergeKey);

    void softDeleteMissingFinanceShareholders(@Param("corporateNumber") String corporateNumber, @Param("financeMergeKey") String financeMergeKey, @Param("syncId") String syncId);

    void softDeleteOrphanedFinanceShareholders(@Param("corporateNumber") String corporateNumber);
}