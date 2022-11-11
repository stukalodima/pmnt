package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Currency;
import com.itk.finance.entity.CurrencyRate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service(CurrencyService.NAME)
public class CurrencyServiceBean implements CurrencyService {
//    private final DataManager dataManager = AppBeans.get(DataManager.class);

    @Override
    public void getCurrencyListFromExternal() throws IOException {
        RestClientService restClientService = AppBeans.get(RestClientService.class);
        ExternalSystemConnectConfig externalSystemConnectConfig = AppBeans.get(ExternalSystemConnectConfig.class);
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCompanyAccounts());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
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
        DataManager dataManager = AppBeans.get(DataManager.class);
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
        return getCurrencyByParam("code", code);
    }

    private Currency getCurrencyByParam(String paramName, String paramValue) {
        DataManager dataManager = AppBeans.get(DataManager.class);
        List<Currency> currencyList = dataManager.load(Currency.class)
                .query("select e from finance_Currency e where e." + paramName + " = :paramValue")
                .parameter("paramValue", paramValue)
                .list();
        Currency currency = null;
        if (currencyList.size() > 0) {
            currency = currencyList.get(0);
        }
        return currency;
    }

    @Override
    public Currency getCurrencyByShortName(String shortName) {
        return getCurrencyByParam("shortName", shortName);
    }

    @Override
    public Double getSumaInUahByCurrency(Currency currency, Date onDate, Double suma) {
        double result = 0.;
        DataManager dataManager = AppBeans.get(DataManager.class);
        Optional<CurrencyRate> currencyRateOptional = dataManager.load(CurrencyRate.class)
                .query("select e from finance_CurrencyRate e " +
                        "where e.currency = :currency " +
                        "and e.onDate = (select max(md.onDate) from finance_CurrencyRate md where md.onDate<= :onDate and md.currency = :currency)")
                .parameter("currency", currency)
                .parameter("onDate", onDate)
                .view("currencyRate-all-property")
                .optional();
        if (currencyRateOptional.isPresent()) {
            CurrencyRate currencyRate = currencyRateOptional.get();
            result = suma * currencyRate.getRate() / currencyRate.getMultiplicity();
        }
        return result;
    }
}