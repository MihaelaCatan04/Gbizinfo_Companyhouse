package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CommendationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommendationMapper {

    void upsertCommendation(CommendationDto dto);

    void upsertCompanyCommendation(CommendationDto dto);

    void softDeleteAllCompanyCommendations(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyCommendations(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}