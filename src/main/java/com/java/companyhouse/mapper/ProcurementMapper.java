package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ProcurementDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcurementMapper {

    void upsertProcurement(ProcurementDto dto);

    void upsertCompanyProcurement(ProcurementDto dto);

    void softDeleteAllCompanyProcurements(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyProcurements(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}