package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.PatentDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatentMapper {

    void bulkUpsertPatents(@Param("list") List<PatentDto> list);

    Long findPatentIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findPatentIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyPatents(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyPatents(@Param("companyIds") List<Long> companyIds);

    void softDeleteMissingCompanyPatents(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}