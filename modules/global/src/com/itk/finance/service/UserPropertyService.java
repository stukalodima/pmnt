package com.itk.finance.service;

import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ManagementCompany;

public interface UserPropertyService {
    String NAME = "finance_UserPropertyService";

    ManagementCompany getDefaultManagementCompany();

    Business getDefaultBusiness();

    Company getDefaultCompany();

}