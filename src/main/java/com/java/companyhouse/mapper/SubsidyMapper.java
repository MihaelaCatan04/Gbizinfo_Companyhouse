package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.SubsidyDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubsidyMapper {

    void bulkUpsertSubsidies(@Param("list") List<SubsidyDto> list);

    Long findSubsidyIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findSubsidyIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanySubsidies(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanySubsidies(@Param("list") List<Long> companyIds);

    void softDeleteMissingCompanySubsidies(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}