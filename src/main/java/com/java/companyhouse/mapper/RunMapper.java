package com.java.companyhouse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RunMapper {
    void insertRunCompany(@Param("runId") String runId,
                          @Param("corporateNumber") String corporateNumber);
    void insertRunPatent(@Param("runId") String runId,
                         @Param("mergeKey") String mergeKey);
    void insertRunClassification(@Param("runId") String runId,
                                 @Param("mergeKey") String mergeKey,
                                 @Param("patentMergeKey") String patentMergeKey);
    void insertRunFinance(@Param("runId") String runId,
                          @Param("corporateNumber") String corporateNumber,
                          @Param("mergeKey") String mergeKey);
    void insertRunMajorShareholder(@Param("runId") String runId,
                                   @Param("financeMergeKey") String financeMergeKey,
                                   @Param("mergeKey") String mergeKey);
    void insertRunManagementIndex(@Param("runId") String runId,
                                  @Param("financeMergeKey") String financeMergeKey,
                                  @Param("mergeKey") String mergeKey);
    void insertRunCommendation(@Param("runId") String runId,
                               @Param("corporateNumber") String corporateNumber,
                               @Param("mergeKey") String mergeKey);
    void insertRunCertification(@Param("runId") String runId,
                                @Param("corporateNumber") String corporateNumber,
                                @Param("mergeKey") String mergeKey);
    void insertRunSubsidy(@Param("runId") String runId,
                          @Param("corporateNumber") String corporateNumber,
                          @Param("mergeKey") String mergeKey);
    void insertRunProcurement(@Param("runId") String runId,
                              @Param("corporateNumber") String corporateNumber,
                              @Param("mergeKey") String mergeKey);
    void insertRunCompanyItem(@Param("runId") String runId,
                              @Param("corporateNumber") String corporateNumber,
                              @Param("mergeKey") String mergeKey);
}