package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CommendationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommendationMapper {

    void upsertCommendation(CommendationDto entity);

    void upsertCompanyCommendation(@Param("companyId") Long companyId, @Param("commendationId") Long commendationId, @Param("syncId") String syncId);

    void softDeleteAllCompanyCommendations(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyCommendations(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findCommendationIdByMergeKey(String mergeKey);
}