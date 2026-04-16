package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ProcurementDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcurementMapper {

    void upsertProcurement(ProcurementDto entity);

    void upsertCompanyProcurement(@Param("companyId") Long companyId, @Param("procurementId") Long procurementId, @Param("syncId") String syncId);

    void softDeleteAllCompanyProcurements(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyProcurements(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findProcurementIdByMergeKey(String mergeKey);
}