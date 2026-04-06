package com.java.companyhouse.model.receiver;

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
@NoArgsConstructor
@AllArgsConstructor
public class CompanySnapshot<T> {
    @NotBlank(message = "corporateNumber is required")
    @Size(min = 13, max = 13, message = "corporateNumber must be 13 characters long")
    private String corporateNumber;
    @Valid
    private List<T> entities;
}
