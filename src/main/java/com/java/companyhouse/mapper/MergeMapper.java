package com.java.companyhouse.mapper;

import com.java.companyhouse.model.dto.*;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MergeMapper {
    void upsertCompany(CompanyDto dto);
    void upsertPatent(PatentDto dto);
    void upsertClassification(ClassificationDto dto);
    void upsertFinance(FinanceDto dto);
    void upsertMajorShareholder(MajorShareholderDto dto);
    void upsertManagementIndex(ManagementIndexDto dto);
    void upsertCommendation(CommendationDto dto);
    void upsertCertification(CertificationDto dto);
    void upsertSubsidy(SubsidyDto dto);
    void upsertProcurement(ProcurementDto dto);
    void upsertItemInfo(ItemInfoDto dto);
    void upsertBaseInfo(BaseInfoDto dto);
    void upsertWomenActivity(WomenActivityInfoDto dto);
    void upsertCompatibility(CompatibilityOfChildcareAndWorkDto dto);
    void upsertWorkplaceInfo(WorkplaceInfoDto dto);

    void upsertCompanyPatent(PatentDto dto);
    void upsertPatentClassification(ClassificationDto dto);
    void upsertCompanyFinance(FinanceDto dto);
    void upsertFinanceShareholder(MajorShareholderDto dto);
    void upsertFinanceManagement(ManagementIndexDto dto);
    void upsertCompanyCommendation(CommendationDto dto);
    void upsertCompanyCertification(CertificationDto dto);
    void upsertCompanySubsidy(SubsidyDto dto);
    void upsertCompanyProcurement(ProcurementDto dto);
    void upsertCompanyItem(ItemInfoDto dto);
    void upsertCompanyWorkplaceInfo(WorkplaceInfoDto dto);
}