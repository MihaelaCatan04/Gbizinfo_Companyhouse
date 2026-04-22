package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CertificationDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CertificationMapper {

    void bulkUpsertCertifications(@Param("list") List<CertificationDto> list);

    Long findCertificationIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findCertificationIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyCertifications(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyCertifications(@Param("companyIds") List<Long> companyIds);

    void softDeleteMissingCompanyCertifications(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}