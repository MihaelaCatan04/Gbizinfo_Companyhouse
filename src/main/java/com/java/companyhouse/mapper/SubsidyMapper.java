package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.SubsidyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubsidyMapper {

    void upsertSubsidy(SubsidyDto entity);

    void upsertCompanySubsidy(@Param("companyId") Long companyId, @Param("subsidyId") Long subsidyId, @Param("syncId") String syncId);

    void softDeleteAllCompanySubsidies(@Param("companyId") Long companyId);

    void softDeleteMissingCompanySubsidies(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findSubsidyIdByMergeKey(String mergeKey);
}