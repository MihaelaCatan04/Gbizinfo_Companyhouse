package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CertificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CertificationMapper {

    void upsertCertification(CertificationDto entity);

    void upsertCompanyCertification(@Param("companyId") Long companyId, @Param("certificationId") Long certificationId, @Param("syncId") String syncId);

    void softDeleteAllCompanyCertifications(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyCertifications(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findCertificationIdByMergeKey(String mergeKey);
}