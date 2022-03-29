package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
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
    @Inject
    private RestClientService restClientService;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;
    @Inject
    private CompanyService companyService;

    @Override
    public void getCashFlowItemListfromExternal() throws IOException {
        //"http://localhost:8080/pmnt/VAADIN/paymentClaim.json"
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCashFlowListUrl());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> cashFlowItemsMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            cashFlowItemsMap.put("id", jsonObject.getAsJsonPrimitive("id").getAsString());
            cashFlowItemsMap.put("name", jsonObject.getAsJsonPrimitive("name").getAsString());
            cashFlowItemsMap.put("company", jsonObject.getAsJsonPrimitive("company").getAsString());

            fillCashFlowItemEntity(cashFlowItemsMap);
        }
    }

    private void fillCashFlowItemEntity(HashMap<String, String> cashFlowItemsMap){
        CashFlowItemBusiness cashFlowItemBusiness = getCashFlowItemsItemsById(cashFlowItemsMap.get("id"));

        if (Objects.isNull(cashFlowItemBusiness)) {
            cashFlowItemBusiness = dataManager.create(CashFlowItemBusiness.class);
        }

        cashFlowItemBusiness.setId(UUID.fromString(cashFlowItemsMap.get("id")));
        cashFlowItemBusiness.setName(cashFlowItemsMap.get("name"));
        cashFlowItemBusiness.setCompany(companyService.getCompanyById(cashFlowItemsMap.get("company")));
        if (Objects.isNull(cashFlowItemBusiness.getCashFlowItem())) {
            cashFlowItemBusiness.setCashFlowItem(dataManager.getReference(
                    CashFlowItem.class, UUID.fromString("a6bed62c-5e4c-5fd5-2e2d-dc2bf3a972fd"))
            );
        }

        dataManager.commit(cashFlowItemBusiness);
    }

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
}