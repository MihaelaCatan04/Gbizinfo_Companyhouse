package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.CompanyDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper {
    void upsertCompany(CompanyDto dto);
}