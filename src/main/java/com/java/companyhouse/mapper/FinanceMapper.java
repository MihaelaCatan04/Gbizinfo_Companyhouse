package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.FinanceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FinanceMapper {

    void upsertFinance(FinanceDto entity);

    void upsertCompanyFinance(@Param("companyId") Long companyId, @Param("financeId") Long financeId, @Param("syncId") String syncId);

    void softDeleteAllCompanyFinances(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyFinances(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findFinanceIdByMergeKey(String mergeKey);
}