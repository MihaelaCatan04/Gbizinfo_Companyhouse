package com.java.companyhouse.model.error;

import java.util.List;

public record ErrorResponse(String code, List<String> messages) {}
