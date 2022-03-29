package com.itk.finance.service;

import com.itk.finance.entity.CashFlowItemBusiness;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public interface CashFlowItemsBusinessService {
    String NAME = "finance_CashFlowItemsBusinessService";

    void getCashFlowItemListfromExternal() throws IOException;
    CashFlowItemBusiness getCashFlowItemsItemsById(String id);
    CashFlowItemBusiness getCashFlowItemsItemsById(UUID id);
}