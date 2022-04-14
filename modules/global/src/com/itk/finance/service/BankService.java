package com.itk.finance.service;

import com.itk.finance.entity.Bank;

import java.io.IOException;

public interface BankService {
    String NAME = "finance_BankService";

    void getBankListFromExternal() throws IOException;
    Bank getBankByMfo(String mfo);
}