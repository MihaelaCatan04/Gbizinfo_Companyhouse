package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.FinanceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FinanceMapper {

    void upsertFinance(FinanceDto dto);

    void upsertCompanyFinance(FinanceDto dto);

    void softDeleteAllCompanyFinances(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyFinances(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}