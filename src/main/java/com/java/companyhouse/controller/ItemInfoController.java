package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.service.ItemInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iteminfos")
@Validated
@Tag(name = "ItemInfo", description = "ItemInfo Entity Endpoint")
public class ItemInfoController extends AbstractBatchController<ItemInfoDto> {

    public ItemInfoController(ItemInfoService service) {
        super(service);
    }
}