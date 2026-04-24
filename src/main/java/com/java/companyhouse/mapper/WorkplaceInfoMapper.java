package com.java.companyhouse.mapper;

import com.java.companyhouse.model.solved.ResolvedWorkplaceInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkplaceInfoMapper {

    void bulkUpsertWorkplaceInfos(@Param("list") List<ResolvedWorkplaceInfoDto> list);

    void bulkAttachCompanyWorkplaceInfos(@Param("list") List<ResolvedWorkplaceInfoDto> list);

    void bulkClearCompanyWorkplaceInfos(@Param("list") List<String> corporateNumbers);
}