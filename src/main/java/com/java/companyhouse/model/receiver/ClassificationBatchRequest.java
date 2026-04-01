package com.java.companyhouse.model.receiver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClassificationBatchRequest {
    private List<PatentClassificationSnapshot> patents;
}