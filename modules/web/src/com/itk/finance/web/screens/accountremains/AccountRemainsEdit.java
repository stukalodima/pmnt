package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Currency;
import com.itk.finance.entity.CurrencyRate;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@UiController("finance_AccountRemains.edit")
@UiDescriptor("account-remains-edit.xml")
@EditedEntityContainer("accountRemainsDc")
@LoadDataBeforeShow
public class AccountRemainsEdit extends StandardEditor<AccountRemains> {
    @Inject
    private DataManager dataManager;
    @Inject
    private TimeSource timeSource;

    @Subscribe
    public void onInitEntity(InitEntityEvent<AccountRemains> event) {
        getBeforSummValue(event.getEntity(), event.getEntity().getAccount(), timeSource.currentTimestamp());
    }

    private void getBeforSummValue(AccountRemains entity, Account account, Date onDate) {
        List<AccountRemains> accountList = dataManager.load(AccountRemains.class)
                .query("select e from finance_AccountRemains e where e.account = :account and e.onDate<:onDate order by e.onDate DESC")
                .parameter("account", account)
                .parameter("onDate", onDate)
                .view("accountRemains-all-property")
                .list();
        AccountRemains accountRemains = null;
        if (accountList.size() > 0) {
            accountRemains = accountList.get(0);
            entity.setSummBefor(accountRemains.getSumm());
        }
    }

    private Double getCurrentRate(Date onDate, String currencyName) {
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
        return currencyRate.getRate();
    }

    @Subscribe("onDateField")
    public void onOnDateFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        getBeforSummValue(getEditedEntity(), getEditedEntity().getAccount(), event.getValue());
    }

    @Subscribe("accountField")
    public void onAccountFieldValueChange(HasValue.ValueChangeEvent<Account> event) {
        getBeforSummValue(getEditedEntity(), event.getValue(), getEditedEntity().getOnDate());
    }

    @Subscribe("summField")
    public void onSummFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        Double summ = event.getValue();
        if (getEditedEntity().getAccount().getCurrency().getShortName().equals("UAH")) {
            getEditedEntity().setSummInUAH(summ);
            getEditedEntity().setSummInUSD(summ * getCurrentRate(getEditedEntity().getOnDate(),"USD"));
            getEditedEntity().setSumInEUR(summ*getCurrentRate(getEditedEntity().getOnDate(),"EUR"));
        } else if (getEditedEntity().getAccount().getCurrency().getShortName().equals("USD")) {
            getEditedEntity().setSummInUSD(summ);
        } else if (getEditedEntity().getAccount().getCurrency().getShortName().equals("EUR")) {
            getEditedEntity().setSumInEUR(summ);
        }
    }
}