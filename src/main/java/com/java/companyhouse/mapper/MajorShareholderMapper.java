package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MajorShareholderMapper {

    void bulkUpsertMajorShareholders(@Param("list") List<MajorShareholderDto> list);

    Long findMajorShareholderIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findMajorShareholderIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertFinanceShareholders(@Param("triples") List<JunctionTriple> triples, @Param("syncId") String syncId);

    void softDeleteMissingFinanceShareholders(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteOrphanedFinanceShareholders(@Param("companyIds") List<Long> companyIds);

    void softDeleteAllFinanceShareholders(@Param("list") List<JunctionPair> pairs);

}