package com.java.companyhouse.service;

import com.java.companyhouse.cache.Cache;
import com.java.companyhouse.mapper.ClassificationMapper;
import com.java.companyhouse.mapper.CompanyMapper;
import com.java.companyhouse.mapper.PatentMapper;
import com.java.companyhouse.model.dto.ClassificationDto;
import com.java.companyhouse.model.receiver.PatentClassificationSnapshot;
import com.java.companyhouse.model.solved.JunctionPair;
import com.java.companyhouse.model.solved.JunctionTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ClassificationService extends AbstractNestedBatchService<PatentClassificationSnapshot, ClassificationDto> {

    private static final String COMPANY_KEY = "company:id:";
    private static final String PATENT_KEY = "patent:id:";
    private static final String CLASSIFICATION_KEY = "classification:id:";

    private final ClassificationMapper classificationMapper;
    private final CompanyMapper companyMapper;
    private final PatentMapper patentMapper;
    private final Cache cache;

    public ClassificationService(ClassificationMapper classificationMapper, CompanyMapper companyMapper, PatentMapper patentMapper, TransactionTemplate transactionTemplate, Cache cache, @Value("${batch.size:500}") int batchSize) {
        super(transactionTemplate, batchSize);
        this.classificationMapper = classificationMapper;
        this.companyMapper = companyMapper;
        this.patentMapper = patentMapper;
        this.cache = cache;
    }

    @Override
    protected void bulkUpsertEntities(List<ClassificationDto> chunk) {
        classificationMapper.bulkUpsertClassifications(chunk);
    }

    @Override
    protected void warmCache(List<String> corporateNumbers, List<String> parentMergeKeys, List<String> childMergeKeys) {
        cache.warmAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
        cache.warmAll(PATENT_KEY, parentMergeKeys, patentMapper::findPatentIdsByMergeKeys);
        cache.warmAll(CLASSIFICATION_KEY, childMergeKeys, classificationMapper::findClassificationIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveCompanyIds(List<String> corporateNumbers) {
        return cache.resolveAll(COMPANY_KEY, corporateNumbers, companyMapper::findCompanyIdsByCorporateNumbers);
    }

    @Override
    protected Map<String, Long> resolveParentIds(List<String> parentMergeKeys) {
        return cache.resolveAll(PATENT_KEY, parentMergeKeys, patentMapper::findPatentIdsByMergeKeys);
    }

    @Override
    protected Map<String, Long> resolveChildIds(List<String> mergeKeys) {
        return cache.resolveAll(CLASSIFICATION_KEY, mergeKeys, classificationMapper::findClassificationIdsByMergeKeys);
    }

    @Override
    protected void bulkUpsertJunction(List<JunctionTriple> triples, String syncId) {
        classificationMapper.bulkUpsertPatentClassifications(triples, syncId);
    }

    @Override
    protected void cleanup(List<JunctionPair> processedPairs, String syncId) {
        classificationMapper.softDeleteMissingPatentClassifications(processedPairs, syncId);
    }

    @Override
    protected void deleteAllBatch(List<PatentClassificationSnapshot> emptySnapshots) {
        Map<String, Long> companyIds = resolveCompanyIds(emptySnapshots.stream().map(PatentClassificationSnapshot::getCorporateNumber).distinct().toList());
        Map<String, Long> patentIds = resolveParentIds(emptySnapshots.stream().map(PatentClassificationSnapshot::getParentMergeKey).distinct().toList());

        List<JunctionPair> pairs = emptySnapshots.stream().map(s -> {
            Long companyId = companyIds.get(s.getCorporateNumber());
            Long patentId = patentIds.get(s.getParentMergeKey());
            return companyId != null && patentId != null ? new JunctionPair(companyId, patentId) : null;
        }).filter(Objects::nonNull).distinct().toList();

        classificationMapper.softDeleteAllPatentClassifications(pairs);
    }

    public void deleteOrphanedPatentClassifications(List<Long> companyIds) {
        classificationMapper.softDeleteOrphanedPatentClassifications(companyIds);
    }

    @Override
    protected String getMergeKey(ClassificationDto entity) {
        return entity.getMergeKey();
    }

    @Override
    protected String getCorporateNumber(ClassificationDto entity) {
        return entity.getCorporateNumber();
    }

    @Override
    protected String getParentMergeKey(ClassificationDto entity) {
        return entity.getPatentMergeKey();
    }
}