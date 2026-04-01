package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CertificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CertificationMapper {

    void upsertCertification(CertificationDto dto);

    void upsertCompanyCertification(@Param("dto") CertificationDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyCertifications(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyCertifications(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}