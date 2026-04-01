package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.PatentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PatentMapper {

    void upsertPatent(PatentDto dto);

    void upsertCompanyPatent(@Param("dto") PatentDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyPatents(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyPatents(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}