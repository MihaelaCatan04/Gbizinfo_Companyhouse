package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ClassificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClassificationMapper {

    void upsertClassification(ClassificationDto dto);

    void upsertPatentClassification(@Param("mergeKey") String mergeKey, @Param("patentMergeKey") String patentMergeKey, @Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);

    void softDeleteAllPatentClassifications(@Param("corporateNumber") String corporateNumber, @Param("patentMergeKey") String patentMergeKey);

    void softDeleteMissingPatentClassifications(@Param("corporateNumber") String corporateNumber, @Param("patentMergeKey") String patentMergeKey, @Param("syncId") String syncId);

    void softDeleteOrphanedPatentClassifications(@Param("corporateNumber") String corporateNumber);
}