package com.java.companyhouse.service;

import com.java.companyhouse.mapper.ItemInfoMapper;
import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.model.receiver.CompanySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemInfoService extends AbstractBatchService<ItemInfoDto> {

    private final ItemInfoMapper itemInfoMapper;

    @Override
    protected void processSnapshot(CompanySnapshot<ItemInfoDto> snapshot) {
        String corporateNumber = snapshot.getCorporateNumber();
        List<ItemInfoDto> itemInfos = snapshot.getEntities();

        if (itemInfos.isEmpty()) {
            itemInfoMapper.softDeleteAllCompanyItems(corporateNumber);
            return;
        }

        String syncId = UUID.randomUUID().toString();

        for (ItemInfoDto dto : itemInfos) {
            itemInfoMapper.upsertItemInfo(dto);
            itemInfoMapper.upsertCompanyItem(dto, syncId);
        }

        itemInfoMapper.softDeleteMissingCompanyItems(corporateNumber, syncId);
    }
}