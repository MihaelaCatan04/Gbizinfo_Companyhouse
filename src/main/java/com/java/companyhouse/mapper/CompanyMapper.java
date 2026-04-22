package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CompanyDto;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {

    void bulkUpsertCompanies(@Param("list") List<CompanyDto> list);

    Long findCompanyIdByCorporateNumber(@Param("corporateNumber") String corporateNumber);

    List<MergeKeyIdDto> findCompanyIdsByCorporateNumbers(@Param("list") List<String> list);
}