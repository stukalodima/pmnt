package com.itk.finance.service;

import com.itk.finance.entity.Account;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Company;

import java.io.IOException;
import java.util.Date;

public interface AccountsService {
    String NAME = "finance_AccountsService";

    void getCompanyAccountsListFromExternal() throws IOException;
    Account getCompanyAccountsByIban(String iban);
    double getCurrentRate(Date onDate, String currencyName);
    double getBeforSummValue(Account account, Date onDate);

}