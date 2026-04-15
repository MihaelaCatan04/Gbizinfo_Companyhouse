package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ItemInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemInfoMapper {

    void upsertItemInfo(ItemInfoDto entity);

    void upsertCompanyItem(@Param("companyId") Long companyId, @Param("infoId") Long infoId, @Param("syncId") String syncId);

    void softDeleteAllCompanyItems(@Param("companyId") Long companyId);

    void softDeleteMissingCompanyItems(@Param("companyId") Long companyId, @Param("syncId") String syncId);

    Long findInfoIdByMergeKey(String mergeKey);
}