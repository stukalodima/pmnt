package com.itk.finance.service;

import com.itk.finance.entity.Company;
import org.springframework.stereotype.Service;

@Service(ProcPropertyService.NAME)
public class ProcPropertyServiceBean implements ProcPropertyService {

    @Override
    public Boolean getUsePaymentApproval(Company company) {
        return false;
    }
}