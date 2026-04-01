package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ClassificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassificationMapper {

    void upsertClassification(ClassificationDto dto);

    void upsertPatentClassification(ClassificationDto dto);

    void softDeleteAllPatentClassifications(@Param("patentMergeKey") String patentMergeKey);

    void softDeleteMissingPatentClassifications(@Param("patentMergeKey") String patentMergeKey, @Param("mergeKeys") List<String> mergeKeys);
}