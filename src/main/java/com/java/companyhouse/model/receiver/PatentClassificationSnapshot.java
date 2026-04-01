package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.ClassificationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PatentClassificationSnapshot {
    private String patentMergeKey;
    private List<ClassificationDto> classifications;
}