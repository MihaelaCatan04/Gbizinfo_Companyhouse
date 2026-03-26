package com.java.companyhouse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeleteMapper {
    void deleteStaleCompanyPatents(@Param("runId") String runId);
    void deleteStalePatentClassifications(@Param("runId") String runId);
    void deleteStaleCompanyFinances(@Param("runId") String runId);
    void deleteStaleFinanceShareholders(@Param("runId") String runId);
    void deleteStaleFinanceManagement(@Param("runId") String runId);
    void deleteStaleCompanyCommendations(@Param("runId") String runId);
    void deleteStaleCompanyCertifications(@Param("runId") String runId);
    void deleteStaleCompanySubsidies(@Param("runId") String runId);
    void deleteStaleCompanyProcurements(@Param("runId") String runId);
    void deleteStaleCompanyItems(@Param("runId") String runId);
    void deleteStaleWorkplaceInfos(@Param("runId") String runId);
    void cleanupRunCompany(@Param("runId") String runId);
    void cleanupRunPatent(@Param("runId") String runId);
    void cleanupRunClassification(@Param("runId") String runId);
    void cleanupRunFinance(@Param("runId") String runId);
    void cleanupRunMajorShareholder(@Param("runId") String runId);
    void cleanupRunManagementIndex(@Param("runId") String runId);
    void cleanupRunCommendation(@Param("runId") String runId);
    void cleanupRunCertification(@Param("runId") String runId);
    void cleanupRunSubsidy(@Param("runId") String runId);
    void cleanupRunProcurement(@Param("runId") String runId);
    void cleanupRunCompanyItem(@Param("runId") String runId);
}