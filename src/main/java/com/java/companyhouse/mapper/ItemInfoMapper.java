package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ItemInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemInfoMapper {

    void upsertItemInfo(ItemInfoDto dto);

    void upsertCompanyItem(@Param("dto") ItemInfoDto dto, @Param("syncId") String syncId);

    void softDeleteAllCompanyItems(@Param("corporateNumber") String corporateNumber);

    void softDeleteMissingCompanyItems(@Param("corporateNumber") String corporateNumber, @Param("syncId") String syncId);
}