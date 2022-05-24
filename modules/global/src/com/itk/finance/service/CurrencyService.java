package com.itk.finance.service;

import com.itk.finance.entity.Currency;

import java.io.IOException;

public interface CurrencyService {
    String NAME = "finance_CurrencyService";

    void getCurrencyListFromExternal() throws IOException;
    Currency getCurrencyByCode(String code);
    Currency getCurrencyByShortName(String shortName);
}