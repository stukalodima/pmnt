package com.itk.finance.service;

import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;

public interface UserPropertyService {
    String NAME = "finance_UserPropertyService";

    Business getDefaulBusiness();

    Company getDefaulCompany();

}