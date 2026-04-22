package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompatibilityMapper {

    void bulkUpsertCompatibilities(@Param("list") List<CompatibilityOfChildcareAndWorkDto> list);

    Long findCompatibilityIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findCompatibilityIdsByMergeKeys(@Param("list") List<String> list);
}