package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Bank;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service(BankService.NAME)
public class BankServiceBean implements BankService {

    @Inject
    private DataManager dataManager;
    @Inject
    private RestClientService restClientService;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;

    @Override
    public void getBankListFromExternal() throws IOException {
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getBankListUrl());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> bankMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.getAsJsonPrimitive("TYP").getAsString().equals("0")
                    || jsonObject.getAsJsonPrimitive("TYP").getAsString().equals("1")) {
                bankMap.put("parentBank", jsonObject.getAsJsonPrimitive("GLMFO").getAsString());
                bankMap.put("name", jsonObject.getAsJsonPrimitive("SHORTNAME").getAsString());
                bankMap.put("mfo", jsonObject.getAsJsonPrimitive("MFO").getAsString());
                bankMap.put("fullName", jsonObject.getAsJsonPrimitive("FULLNAME").getAsString());
                bankMap.put("stan", jsonObject.getAsJsonPrimitive("N_STAN").getAsString());
                fillBankEntity(bankMap);
            }
        }
    }

    private void fillBankEntity(HashMap<String, String> bankMap) {
        Bank bank = getBankByMfo(bankMap.get("mfo"));

        if (Objects.isNull(bank)) {
            bank = dataManager.create(Bank.class);
        }
        if (bankMap.get("parentBank").equals(bankMap.get("mfo"))) {
            bank.setParentBank(bank);
        } else {
            Bank parentBank = getBankByMfo(bankMap.get("parentBank"));
            bank.setParentBank(parentBank);
        }
        bank.setName(bankMap.get("name"));
        bank.setMfo(bankMap.get("mfo"));
        bank.setFullName(bankMap.get("fullName"));
        bank.setStan(bankMap.get("stan"));

        dataManager.commit(bank);
    }

    @Override
    public Bank getBankByMfo(String mfo) {
        List<Bank> bankList = dataManager.load(Bank.class)
                .query("select e from finance_Bank e where e.mfo = :mfo")
                .parameter("mfo", mfo)
                .view("bank-full")
                .list();
        Bank bank = null;
        if (bankList.size() > 0) {
            bank = bankList.get(0);
        }
        return bank;
    }
}