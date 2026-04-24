package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WomenActivityMapper {

    void bulkUpsertWomenActivities(@Param("list") List<WomenActivityInfoDto> list);

    Long findWomenActivityIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findWomenActivityIdsByMergeKeys(@Param("list") List<String> list);
}