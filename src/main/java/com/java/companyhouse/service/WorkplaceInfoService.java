package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.WorkplaceInfoMapper;
import com.java.companyhouse.model.dto.WorkplaceInfoDto;
import com.java.companyhouse.model.solved.ResolvedWorkplaceInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WorkplaceInfoService extends AbstractBatchService<WorkplaceInfoDto> {

    private static final String COMPANY_KEY = "company:id:";

    private final WorkplaceInfoMapper workplaceInfoMapper;
    private final CompanyMapper companyMapper;
    private final BaseInfoService baseInfoService;
    private final WomenActivityService womenActivityService;
    private final CompatibilityService compatibilityService;
    private final Cache cache;

    public WorkplaceInfoService(WorkplaceInfoMapper workplaceInfoMapper, CompanyMapper companyMapper, BaseInfoService baseInfoService, WomenActivityService womenActivityService, CompatibilityService compatibilityService, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.workplaceInfoMapper = workplaceInfoMapper;
        this.companyMapper = companyMapper;
        this.baseInfoService = baseInfoService;
        this.womenActivityService = womenActivityService;
        this.compatibilityService = compatibilityService;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<WorkplaceInfoDto> chunk) {
        Map<String, Long> baseInfoIds = baseInfoService.resolveBaseInfoIds(chunk.stream().map(WorkplaceInfoDto::getBaseInfoMergeKey).toList());
        Map<String, Long> womenActivityIds = womenActivityService.resolveWomenActivityIds(chunk.stream().map(WorkplaceInfoDto::getWomenActivityMergeKey).toList());
        Map<String, Long> compatibilityIds = compatibilityService.resolveCompatibilityIds(chunk.stream().map(WorkplaceInfoDto::getCompatibilityMergeKey).toList());

        List<ResolvedWorkplaceInfoDto> resolved = chunk.stream().map(e -> new ResolvedWorkplaceInfoDto(e, baseInfoIds.get(e.getBaseInfoMergeKey()), womenActivityIds.get(e.getWomenActivityMergeKey()), compatibilityIds.get(e.getCompatibilityMergeKey()))).toList();

        workplaceInfoMapper.bulkUpsertWorkplaceInfos(resolved);
        workplaceInfoMapper.bulkAttachCompanyWorkplaceInfos(resolved);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> mergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected void onEmptyBatch(List<String> corporateNumbers) {
        workplaceInfoMapper.bulkClearCompanyWorkplaceInfos(corporateNumbers);
    }

    @Override
    protected String getMergeKey(WorkplaceInfoDto e) {
        return e.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(WorkplaceInfoDto e) {
        return e.getCorporateNumber();
    }
}