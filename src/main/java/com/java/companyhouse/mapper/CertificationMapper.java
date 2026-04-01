package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CertificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CertificationMapper {

    void upsertCertification(CertificationDto dto);

    void upsertCompanyCertification(CertificationDto dto);

    void softDeleteAllCompanyCertifications(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyCertifications(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}