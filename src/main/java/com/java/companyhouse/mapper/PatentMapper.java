package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.PatentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatentMapper {

    void upsertPatent(PatentDto dto);

    void upsertCompanyPatent(PatentDto dto);

    void softDeleteAllCompanyPatents(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyPatents(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}