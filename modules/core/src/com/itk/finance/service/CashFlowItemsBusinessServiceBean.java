package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.CashFlowItem;
import com.itk.finance.entity.CashFlowItemBusiness;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service(CashFlowItemsBusinessService.NAME)
public class CashFlowItemsBusinessServiceBean implements CashFlowItemsBusinessService {

    @Inject
    private DataManager dataManager;

    @Override
    public CashFlowItemBusiness getCashFlowItemsItemsById(String id) {
        UUID uuid = UUID.fromString(id);
        return getCashFlowItemsItemsById(uuid);
    }

    @Override
    public CashFlowItemBusiness getCashFlowItemsItemsById(UUID id) {
        CashFlowItemBusiness cashFlowItemBusiness = null;
        List<CashFlowItemBusiness> cashFlowItemBusinessList = dataManager.load(CashFlowItemBusiness.class)
                .query("select e from finance_CashFlowItemBusiness e where e.id = :id")
                .parameter("id", id)
                .view("cashFlowItemBusiness-all-property")
                .list();
        if (cashFlowItemBusinessList.size() > 0) {
            cashFlowItemBusiness = cashFlowItemBusinessList.get(0);
        }
        return cashFlowItemBusiness;
    }

    @Override
    public CashFlowItemBusiness getCashFlowItemsItemsByName(String name, Business business) {
        CashFlowItemBusiness cashFlowItemBusiness = null;
        List<CashFlowItemBusiness> cashFlowItemBusinessList = dataManager.load(CashFlowItemBusiness.class)
                .query("select e from finance_CashFlowItemBusiness e where e.name = :name and e.business = :business")
                .parameter("name", name)
                .parameter("business", business)
                .view("cashFlowItemBusiness-all-property")
                .list();
        if (cashFlowItemBusinessList.size() > 0) {
            cashFlowItemBusiness = cashFlowItemBusinessList.get(0);
        }
        return cashFlowItemBusiness;
    }
}