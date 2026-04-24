package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ManagementIndexDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManagementIndexMapper {

    void bulkUpsertManagementIndexes(@Param("list") List<ManagementIndexDto> list);

    Long findManagementIndexIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findManagementIndexIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertFinanceManagementIndexes(@Param("triples") List<JunctionTriple> triples, @Param("syncId") String syncId);

    void softDeleteMissingFinanceManagementIndexes(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteOrphanedFinanceManagementIndexes(@Param("companyIds") List<Long> companyIds);

    void softDeleteAllFinanceManagementIndexes(@Param("list") List<JunctionPair> pairs);

}