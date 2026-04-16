package com.java.companyhouse.model.receiver;

import com.java.companyhouse.model.dto.ClassificationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatentClassificationSnapshot implements NestedSnapshot<ClassificationDto> {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    private String corporateNumber;
    @NotBlank(message = "patentMergeKey is required")
    private String patentMergeKey;
    @Valid
    private List<ClassificationDto> classifications;

    @Override
    public String getParentMergeKey() {
        return patentMergeKey;
    }

    @Override
    public List<ClassificationDto> getItems() {
        return classifications;
    }
}