package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ClassificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClassificationMapper {

    void upsertClassification(ClassificationDto dto);

    void upsertPatentClassification(@Param("companyId") Long companyId, @Param("patentId") Long patentId, @Param("classificationId") Long classificationId, @Param("syncId") String syncId);

    void softDeleteAllPatentClassifications(@Param("companyId") Long companyId, @Param("patentId") Long patentId);

    void softDeleteMissingPatentClassifications(@Param("companyId") Long companyId, @Param("patentId") Long patentId, @Param("syncId") String syncId);

    void softDeleteOrphanedPatentClassifications(@Param("companyId") Long companyId);

    Long findClassificationIdByMergeKey(String mergeKey);
}