package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CommendationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommendationMapper {

    void upsertCommendation(CommendationDto dto);

    void upsertCompanyCommendation(@Param("dto") CommendationDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyCommendations(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyCommendations(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}