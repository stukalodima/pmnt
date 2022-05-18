package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Account;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service(AccountsService.NAME)
public class AccountsServiceBean implements AccountsService {

    @Inject
    private RestClientService restClientService;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;
    @Inject
    private DataManager dataManager;
    @Inject
    private CompanyService companyService;
    @Inject
    private CurrencyService currencyService;
    @Inject
    private BankService bankService;

    @Override
    public void getCompanyAccountsListFromExternal() throws IOException {
        //"http://localhost:8080/pmnt/VAADIN/company.json"
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCompanyAccounts());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    @Override
    public Account getCompanyAccountsByIban(String iban) {
        List<Account> accountList = dataManager.load(Account.class)
                .query("select e from finance_Account e where e.iban = :iban")
                .parameter("iban", iban)
                .view("account-all-property")
                .list();
        Account account = null;
        if (accountList.size() > 0) {
            account = accountList.get(0);
        }
        return account;
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> accountsMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            accountsMap.put("iban", jsonObject.getAsJsonPrimitive("iban").getAsString());
            accountsMap.put("company", jsonObject.getAsJsonPrimitive("company").getAsString());
            accountsMap.put("name", jsonObject.getAsJsonPrimitive("name").getAsString());
            accountsMap.put("currency_code", jsonObject.getAsJsonPrimitive("currency_code").getAsString());
            accountsMap.put("bank_mfo", jsonObject.getAsJsonPrimitive("bank_mfo").getAsString());

            fillAccountEntity(accountsMap);
        }
    }

    private void fillAccountEntity(HashMap<String, String> accountsMap) {
        Account account = getCompanyAccountsByIban(accountsMap.get("iban"));

        if (Objects.isNull(account)) {
            account = dataManager.create(Account.class);
        }

        account.setCompany(companyService.getCompanyById(accountsMap.get("company")));
        account.setCurrency(currencyService.getCurrencyByCode(accountsMap.get("currency_code")));
        account.setIban(accountsMap.get("iban"));
        account.setBank(bankService.getBankByMfo(accountsMap.get("bank_mfo")));

        dataManager.commit(account);
    }
}