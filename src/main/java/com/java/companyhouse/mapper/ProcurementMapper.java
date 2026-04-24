package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ProcurementDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcurementMapper {

    void bulkUpsertProcurements(@Param("list") List<ProcurementDto> list);

    Long findProcurementIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findProcurementIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyProcurements(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyProcurements(@Param("list") List<Long> companyIds);

    void softDeleteMissingCompanyProcurements(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}