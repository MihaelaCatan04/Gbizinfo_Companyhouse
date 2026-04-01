package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkplaceInfoMapper {
    void upsertWorkplaceInfo(WorkplaceInfoDto dto);

    void upsertCompanyWorkplaceInfo(WorkplaceInfoDto dto);

    void clearCompanyWorkplaceInfo(@Param("corporateNumber") String corporateNumber);
}