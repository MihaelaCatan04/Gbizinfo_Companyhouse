package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.SubsidyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubsidyMapper {

    void upsertSubsidy(SubsidyDto dto);

    void upsertCompanySubsidy(@Param("dto") SubsidyDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanySubsidies(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanySubsidies(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}