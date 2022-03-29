package com.itk.finance.service;

import com.itk.finance.entity.Company;

import java.io.IOException;

public interface CompanyService {
    String NAME = "finance_CompanyService";

    void getCompanyListFromExternal() throws IOException;
    Company getCompanyByEdrpou(String edrpou);
    Company getCompanyById(String id);
}