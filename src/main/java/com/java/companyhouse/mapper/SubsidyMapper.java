package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.SubsidyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubsidyMapper {

    void upsertSubsidy(SubsidyDto dto);

    void upsertCompanySubsidy(SubsidyDto dto);

    void softDeleteAllCompanySubsidies(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanySubsidies(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}