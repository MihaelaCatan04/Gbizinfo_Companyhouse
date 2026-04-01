package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.BaseInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseInfoMapper {
    void upsertBaseInfo(BaseInfoDto dto);
}