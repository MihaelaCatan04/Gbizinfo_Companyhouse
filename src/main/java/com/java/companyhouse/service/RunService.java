package com.java.companyhouse.service;

import com.java.companyhouse.mapper.DeleteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RunService {

    private final DeleteMapper deleteMapper;

    @Transactional
    public void completeEntity(String runId, String entity) {
        switch (entity) {
            case "patents"        -> {
                deleteMapper.deleteStaleCompanyPatents(runId);
                deleteMapper.deleteStalePatentClassifications(runId);
            }
            case "finances"       -> {
                deleteMapper.deleteStaleCompanyFinances(runId);
                deleteMapper.deleteStaleFinanceShareholders(runId);
                deleteMapper.deleteStaleFinanceManagement(runId);
            }
            case "commendations"  -> deleteMapper.deleteStaleCompanyCommendations(runId);
            case "certifications" -> deleteMapper.deleteStaleCompanyCertifications(runId);
            case "subsidies"      -> deleteMapper.deleteStaleCompanySubsidies(runId);
            case "procurements"   -> deleteMapper.deleteStaleCompanyProcurements(runId);
            case "item-infos"     -> deleteMapper.deleteStaleCompanyItems(runId);
            case "workplace-infos"-> deleteMapper.deleteStaleWorkplaceInfos(runId);
        }
    }

    @Transactional
    public void completeRun(String runId) {
        deleteMapper.cleanupRunCompany(runId);
        deleteMapper.cleanupRunPatent(runId);
        deleteMapper.cleanupRunClassification(runId);
        deleteMapper.cleanupRunFinance(runId);
        deleteMapper.cleanupRunMajorShareholder(runId);
        deleteMapper.cleanupRunManagementIndex(runId);
        deleteMapper.cleanupRunCommendation(runId);
        deleteMapper.cleanupRunCertification(runId);
        deleteMapper.cleanupRunSubsidy(runId);
        deleteMapper.cleanupRunProcurement(runId);
        deleteMapper.cleanupRunCompanyItem(runId);
    }
}