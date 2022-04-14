package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Currency;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service(CurrencyService.NAME)
public class CurrencyServiceBean implements CurrencyService {

    @Inject
    private RestClientService restClientService;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;
    @Inject
    private DataManager dataManager;

    @Override
    public void getCurrencyListFromExternal() throws IOException {
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCompanyAccounts());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> currencyMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            currencyMap.put("name", jsonObject.getAsJsonPrimitive("currency_name").getAsString());
            currencyMap.put("code", jsonObject.getAsJsonPrimitive("currency_code").getAsString());
            currencyMap.put("shortName", jsonObject.getAsJsonPrimitive("currency_shortName").getAsString());

            fillCurrencyEntity(currencyMap);
        }
    }

    private void fillCurrencyEntity(HashMap<String, String> currencyMap) {
        Currency currency = getCurrencyByCode(currencyMap.get("code"));

        if (Objects.isNull(currency)) {
            currency = dataManager.create(Currency.class);
        }

        currency.setName(currencyMap.get("name"));
        currency.setShortName(currencyMap.get("shortName"));
        currency.setCode(currencyMap.get("code"));

        dataManager.commit(currency);
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        List<Currency> currencyList = dataManager.load(Currency.class)
                .query("select e from finance_Currency e where e.code = :code")
                .parameter("code", code)
                .list();
        Currency currency = null;
        if (currencyList.size() > 0) {
            currency = currencyList.get(0);
        }
        return currency;
    }
}