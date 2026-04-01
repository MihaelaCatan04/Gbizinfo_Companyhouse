package com.java.companyhouse.service;

import com.java.companyhouse.mapper.CompatibilityMapper;
import com.java.companyhouse.model.dto.CompatibilityOfChildcareAndWorkDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompatibilityService extends AbstractBatchService<CompatibilityOfChildcareAndWorkDto> {

    private final CompatibilityMapper compatibilityMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<CompatibilityOfChildcareAndWorkDto> snapshot) {
        snapshot.getEntities().forEach(compatibilityMapper::upsertCompatibility);
    }
}