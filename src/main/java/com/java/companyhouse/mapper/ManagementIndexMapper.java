package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ManagementIndexMapper {

    void upsertManagementIndex(ManagementIndexDto dto);

    void upsertFinanceManagement(@Param("mergeKey") String mergeKey, @Param("financeMergeKey") String financeMergeKey, @Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);

    void softDeleteAllFinanceManagementIndexes(@Param("corporateNumber") String corporateNumber, @Param("financeMergeKey") String financeMergeKey);

    void softDeleteMissingFinanceManagementIndexes(@Param("corporateNumber") String corporateNumber, @Param("financeMergeKey") String financeMergeKey, @Param("syncId") String syncId);
}