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
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCompanyAccounts());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> bankMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            bankMap.put("name", jsonObject.getAsJsonPrimitive("bank_name").getAsString());
            bankMap.put("mfo", jsonObject.getAsJsonPrimitive("bank_mfo").getAsString());

            fillBankEntity(bankMap);
        }
    }

    private void fillBankEntity(HashMap<String, String> bankMap) {
        Bank bank = getBankByMfo(bankMap.get("mfo"));

        if (Objects.isNull(bank)) {
            bank = dataManager.create(Bank.class);
        }

        bank.setName(bankMap.get("name"));
        bank.setMfo(bankMap.get("mfo"));

        dataManager.commit(bank);
    }

    @Override
    public Bank getBankByMfo(String mfo) {
        List<Bank> bankList = dataManager.load(Bank.class)
                .query("select e from finance_Bank e where e.mfo = :mfo")
                .parameter("mfo", mfo)
                .list();
        Bank bank = null;
        if (bankList.size() > 0) {
            bank = bankList.get(0);
        }
        return bank;
    }
}