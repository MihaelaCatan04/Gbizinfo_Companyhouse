package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManagementIndexMapper {

    void upsertManagementIndex(ManagementIndexDto dto);

    void upsertFinanceManagement(ManagementIndexDto dto);

    void softDeleteAllFinanceManagementIndexes(@Param("financeMergeKey") String financeMergeKey);

    void softDeleteMissingFinanceManagementIndexes(@Param("financeMergeKey") String financeMergeKey, @Param("mergeKeys") List<String> mergeKeys);
}