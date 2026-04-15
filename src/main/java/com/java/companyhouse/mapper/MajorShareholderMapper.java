package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MajorShareholderMapper {

    void upsertMajorShareholder(MajorShareholderDto dto);

    void upsertFinanceShareholder(@Param("companyId") Long companyId, @Param("financeId") Long financeId, @Param("majorShareholderId") Long majorShareholderId, @Param("syncId") String syncId);

    void softDeleteAllFinanceShareholders(@Param("companyId") Long companyId, @Param("financeId") Long financeId);

    void softDeleteMissingFinanceShareholders(@Param("companyId") Long companyId, @Param("financeId") Long financeId, @Param("syncId") String syncId);

    void softDeleteOrphanedFinanceShareholders(@Param("companyId") Long companyId);

    Long findMajorShareholderIdByMergeKey(String mergeKey);
}