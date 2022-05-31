package com.itk.finance.service;

import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ManagementCompany;

public interface UserPropertyService {
    String NAME = "finance_UserPropertyService";

    ManagementCompany getDefaultManagementCompany();

    Business getDefaultBusiness();

    Company getDefaultCompany();

    boolean dontSendEmailByTask();

    boolean dontSendEmailByTask(User user);

    boolean dontSendEmailByApprovalResult();

    boolean dontSendEmailByApprovalResult(User user);
}