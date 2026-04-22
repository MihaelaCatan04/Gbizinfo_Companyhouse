package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.FinanceDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FinanceMapper {

    void bulkUpsertFinances(@Param("list") List<FinanceDto> list);

    Long findFinanceIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findFinanceIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyFinances(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyFinances(@Param("companyIds") List<Long> companyIds);

    void softDeleteMissingCompanyFinances(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}