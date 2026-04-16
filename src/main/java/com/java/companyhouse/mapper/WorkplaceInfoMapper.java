package com.java.companyhouse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkplaceInfoMapper {

    void upsertWorkplaceInfo(@Param("mergeKey") String mergeKey, @Param("baseInfoId") Long baseInfoId, @Param("womenActivityInfoId") Long womenActivityInfoId, @Param("compatibilityId") Long compatibilityId);

    void upsertCompanyWorkplaceInfo(@Param("mergeKey") String mergeKey, @Param("corporateNumber") String corporateNumber);

    void clearCompanyWorkplaceInfo(String corporateNumber);

    String findCompanyWorkplaceInfo(String corporateNumber);
}