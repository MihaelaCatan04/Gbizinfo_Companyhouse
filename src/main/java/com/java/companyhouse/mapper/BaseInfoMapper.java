package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.BaseInfoDto;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseInfoMapper {

    void bulkUpsertBaseInfos(@Param("list") List<BaseInfoDto> list);

    Long findBaseInfoIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findBaseInfoIdsByMergeKeys(@Param("list") List<String> list);
}