package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ManagementIndexMapper {

    void upsertManagementIndex(ManagementIndexDto dto);

    void upsertFinanceManagement(@Param("companyId") Long companyId, @Param("financeId") Long financeId, @Param("managementIndexId") Long managementIndexId, @Param("syncId") String syncId);

    void softDeleteAllFinanceManagementIndexes(@Param("companyId") Long companyId, @Param("financeId") Long financeId);

    void softDeleteMissingFinanceManagementIndexes(@Param("companyId") Long companyId, @Param("financeId") Long financeId, @Param("syncId") String syncId);

    void softDeleteOrphanedFinanceManagementIndexes(@Param("companyId") Long companyId);

    Long findManagementIndexIdByMergeKey(String mergeKey);
}