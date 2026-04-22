package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CommendationDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommendationMapper {

    void bulkUpsertCommendations(@Param("list") List<CommendationDto> list);

    Long findCommendationIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findCommendationIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyCommendations(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyCommendations(@Param("companyIds") List<Long> companyIds);

    void softDeleteMissingCompanyCommendations(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}