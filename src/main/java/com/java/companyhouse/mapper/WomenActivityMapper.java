package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.WomenActivityInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WomenActivityMapper {
    void upsertWomenActivity(WomenActivityInfoDto dto);

    Long findWomenActivityIdByMergeKey(String mergeKey);
}