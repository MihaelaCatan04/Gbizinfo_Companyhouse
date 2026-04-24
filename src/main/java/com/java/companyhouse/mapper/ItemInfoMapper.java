package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemInfoMapper {

    void bulkUpsertItemInfos(@Param("list") List<ItemInfoDto> list);

    Long findInfoIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findItemInfoIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertCompanyItems(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteAllCompanyItems(@Param("list") List<Long> companyIds);

    void softDeleteMissingCompanyItems(@Param("companyIds") List<Long> companyIds, @Param("syncId") String syncId);
}