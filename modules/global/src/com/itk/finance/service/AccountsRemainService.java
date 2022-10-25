package com.itk.finance.service;

import com.itk.finance.entity.Bank;
import com.itk.finance.entity.BankGroup;
import com.itk.finance.entity.Business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface AccountsRemainService {
    String NAME = "finance_AccountsRemainService";

    Double getAccountsRemainOnDate(Date onDate);

    Map<Business, Double> getAccountsRemainByGlBusiness(Date onDate);

    Map<BankGroup, Map<Bank, Map<Business, Double>>> getAccountsRemainByBankGroup(Date onDate);

    Map<Bank, Map<Business, Double>> getAccountsRemainByEmptyBankGroup(Date onDate);

    Map<Bank, Map<Business, Double>> getAccountsRemainByBlockBank(Date onDate);
}