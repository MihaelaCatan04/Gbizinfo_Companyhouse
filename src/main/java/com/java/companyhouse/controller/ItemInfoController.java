package com.java.companyhouse.controller;

import com.java.companyhouse.model.dto.ItemInfoDto;
import com.java.companyhouse.service.ItemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item-infos")
@RequiredArgsConstructor
public class ItemInfoController {
    private final ItemInfoService itemInfoService;

    @PostMapping
    public void receive(@RequestBody List<ItemInfoDto> list) {
        itemInfoService.receive(list);
    }
}