package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ProcurementDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcurementMapper {

    void upsertProcurement(ProcurementDto dto);

    void upsertCompanyProcurement(@Param("dto") ProcurementDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyProcurements(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyProcurements(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}