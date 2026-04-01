package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.FinanceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FinanceMapper {

    void upsertFinance(FinanceDto dto);

    void upsertCompanyFinance(@Param("dto") FinanceDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyFinances(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyFinances(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}