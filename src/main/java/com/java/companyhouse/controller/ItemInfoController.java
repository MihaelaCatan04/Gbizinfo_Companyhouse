package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.service.ItemInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item-infos")
public class ItemInfoController extends AbstractBatchController<ItemInfoDto> {

    public ItemInfoController(ItemInfoService service) {
        super(service);
    }
}