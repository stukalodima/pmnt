package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Account;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Currency;
import com.itk.finance.entity.CurrencyRate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;
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
    public Account getCompanyAccountsByIban(String iban, Currency currency) {
        List<Account> accountList = dataManager.load(Account.class)
                .query("select e from finance_Account e where e.iban = :iban and e.currency = :currency")
                .parameter("iban", iban)
                .parameter("currency", currency)
                .view("account-all-property")
                .list();
        Account account = null;
        if (accountList.size() > 0) {
            account = accountList.get(0);
        }
        return account;
    }

    @Override
    public Account getCompanyAccountsByIban(String iban, String currencyName) {
        Account account = null;
        Currency currency = currencyService.getCurrencyByShortName(currencyName);
        if (!Objects.isNull(currency)) {
            account = getCompanyAccountsByIban(iban, currency);
        }
        return account;
    }

    @Override
    public double getCurrentRate(Date onDate, String currencyName) {
        Currency currency = dataManager.load(Currency.class)
                .query("select e from finance_Currency e where e.shortName = :currencyName")
                .parameter("currencyName", currencyName)
                .one();
        CurrencyRate currencyRate = dataManager.load(CurrencyRate.class)
                .query("select e from finance_CurrencyRate e where e.currency =:currency " +
                        "and e.onDate = (select max(c.onDate) from finance_CurrencyRate c where c.onDate <= :onDate and c.currency=e.currency)")
                .parameter("currency", currency)
                .parameter("onDate", onDate)
                .view("currencyRate-all-property")
                .one();
        return currencyRate.getRate()/(!Objects.isNull(currencyRate.getMultiplicity()) && currencyRate.getMultiplicity()!=0 ? currencyRate.getMultiplicity(): 1);
    }

    @Override
    public double getBeforSummValue(Account account, Date onDate) {
        double result;
        if (Objects.isNull(account)) {
            result = 0.;
        } else {
            List<AccountRemains> accountList = dataManager.load(AccountRemains.class)
                    .query("select e from finance_AccountRemains e where e.account = :account and e.onDate<:onDate order by e.onDate DESC")
                    .parameter("account", account)
                    .parameter("onDate", onDate)
                    .view("accountRemains-all-property")
                    .list();
            if (accountList.size() > 0 && !Objects.isNull(accountList.get(0).getSummEqualsUAH())) {
                result = accountList.get(0).getSummEqualsUAH();
            } else {
                result = 0.;
            }
        }
        return result;
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
        Currency currency = currencyService.getCurrencyByCode(accountsMap.get("currency_code"));
        Account account = getCompanyAccountsByIban(accountsMap.get("iban"),currency);

        if (Objects.isNull(account)) {
            account = dataManager.create(Account.class);
        }

        account.setCompany(companyService.getCompanyById(accountsMap.get("company")));
        account.setCurrency(currency);
        account.setIban(accountsMap.get("iban"));
        account.setBank(bankService.getBankByMfo(accountsMap.get("bank_mfo")));

        dataManager.commit(account);
    }
}