package com.itk.finance.service;

import com.itk.finance.entity.Company;

public interface ProcPropertyService {
    String NAME = "finance_ProcPropertyService";

    Boolean getUsePaymentApproval(Company company);
}