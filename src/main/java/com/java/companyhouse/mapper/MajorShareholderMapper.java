package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.MajorShareholderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MajorShareholderMapper {

    void upsertMajorShareholder(MajorShareholderDto dto);

    void upsertFinanceShareholder(MajorShareholderDto dto);

    void softDeleteAllFinanceShareholders(@Param("financeMergeKey") String financeMergeKey);

    void softDeleteMissingFinanceShareholders(@Param("financeMergeKey") String financeMergeKey, @Param("mergeKeys") List<String> mergeKeys);
}