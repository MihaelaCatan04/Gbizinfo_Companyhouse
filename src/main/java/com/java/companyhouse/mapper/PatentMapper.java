package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.PatentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PatentMapper {

    void upsertPatent(PatentDto entity);

    void upsertCompanyPatent(@Param("companyId") Long companyId, @Param("patentId") Long patentId, @Param("syncId") String syncId);

    void softDeleteAllCompanyPatents(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyPatents(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findPatentIdByMergeKey(String mergeKey);
}