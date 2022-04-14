package com.itk.finance.service;

import com.itk.finance.entity.Account;
import com.itk.finance.entity.Company;

import java.io.IOException;

public interface AccountsService {
    String NAME = "finance_AccountsService";

    void getCompanyAccountsListFromExternal() throws IOException;
    Account getCompanyAccountsByIban(String iban);
}