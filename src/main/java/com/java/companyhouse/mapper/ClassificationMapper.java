package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import com.java.companyhouse.model.solved.MergeKeyIdDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassificationMapper {

    void bulkUpsertClassifications(@Param("list") List<ClassificationDto> list);

    Long findClassificationIdByMergeKey(@Param("mergeKey") String mergeKey);

    List<MergeKeyIdDto> findClassificationIdsByMergeKeys(@Param("list") List<String> list);

    void bulkUpsertPatentClassifications(@Param("triples") List<JunctionTriple> triples, @Param("syncId") String syncId);

    void softDeleteMissingPatentClassifications(@Param("pairs") List<JunctionPair> pairs, @Param("syncId") String syncId);

    void softDeleteOrphanedPatentClassifications(@Param("companyIds") List<Long> companyIds);

    void softDeleteAllPatentClassifications(@Param("list") List<JunctionPair> pairs);
}