package com.itk.finance.service;

import com.itk.finance.entity.Account;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.Currency;

import java.io.IOException;
import java.util.Date;

public interface AccountsService {
    String NAME = "finance_AccountsService";

    void getCompanyAccountsListFromExternal() throws IOException;
    Account getCompanyAccountsByIban(String iban, Currency currency);
    Account getCompanyAccountsByIban(String iban, String currency);
    double getCurrentRate(Date onDate, String currencyName);
    double getBeforSummValue(Account account, Date onDate);

}