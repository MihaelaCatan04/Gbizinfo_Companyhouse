package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ItemInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemInfoMapper {

    void upsertItemInfo(ItemInfoDto dto);

    void upsertCompanyItem(ItemInfoDto dto);

    void softDeleteAllCompanyItems(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyItems(@Param("corporateNumber") String corporateNumber, @Param("mergeKeys") List<String> mergeKeys);
}