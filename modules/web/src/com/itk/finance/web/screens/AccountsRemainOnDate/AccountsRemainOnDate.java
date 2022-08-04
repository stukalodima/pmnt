package com.itk.finance.web.screens.AccountsRemainOnDate;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.Currency;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@UiController("finance_AccountsRemainOnDate")
@UiDescriptor("accounts-remain-on-date.xml")
public class AccountsRemainOnDate extends Screen {
    @Inject
    private CollectionLoader<AccountRemains> accountRemainsesDl;
    @Inject
    private DateField<Date> onDate;
    @Inject
    private TimeSource timeSource;
    @Inject
    private LookupField<Business> business;
    @Inject
    private LookupField<Company> company;
    @Inject
    private LookupField<Currency> currency;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private CollectionLoader<Currency> currenciesDl;
    @Inject
    private GroupTable<AccountRemains> accountRemainsesTable;
    @Inject
    private Messages messages;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        onDate.setValue(timeSource.currentTimestamp());
        setColumnName(onDate.getValue());
        reloadData(business.getValue(), company.getValue(), currency.getValue(), null);
    }

    private void reloadData(Business business, Company company, Currency currency, Date date) {
        if (!Objects.isNull(business)) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
        if (!Objects.isNull(company)) {
            accountRemainsesDl.setParameter("company", company);
        } else {
            accountRemainsesDl.removeParameter("company");
        }
        if (!Objects.isNull(currency)) {
            accountRemainsesDl.setParameter("currency", currency);
        } else {
            accountRemainsesDl.removeParameter("currency");
        }
        if (!Objects.isNull(date)) {
            accountRemainsesDl.setParameter("onDate", date);
        } else {
            accountRemainsesDl.setParameter("onDate", timeSource.currentTimestamp());
        }
        accountRemainsesDl.load();
        businessesDl.load();
        companiesDl.load();
        currenciesDl.load();
    }

    @Subscribe("onDate")
    public void onOnDateValueChange(HasValue.ValueChangeEvent<Date> event) {
        setColumnName(event.getValue());
        reloadData(business.getValue(), company.getValue(), currency.getValue(), event.getValue());
    }

    @Subscribe("business")
    public void onBusinessValueChange(HasValue.ValueChangeEvent<Business> event) {
        reloadData(event.getValue(), company.getValue(), currency.getValue(), onDate.getValue());
        if (!Objects.isNull(event.getValue())) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
    }

    @Subscribe("company")
    public void onCompanyValueChange(HasValue.ValueChangeEvent<Company> event) {
        reloadData(business.getValue(), event.getValue(), currency.getValue(), onDate.getValue());
    }

    @Subscribe("currency")
    public void onCurrencyValueChange(HasValue.ValueChangeEvent<Currency> event) {
        reloadData(business.getValue(), company.getValue(), event.getValue(), onDate.getValue());
    }

    private void setColumnName(Date columnDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date beforColumnDate = addDays(columnDate, -1);
        accountRemainsesTable.getColumn("summBefor")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summBefor.caption") +
                        "<br>" + dateFormat.format(beforColumnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summEqualsUAH")
                .setCaption("<div style=\"text-align: right; color: green;\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summEqualsUAH.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUAH")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInUAH.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUSD")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInUSD.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (USD)" + "</div"
                );
        accountRemainsesTable.getColumn("summInEUR")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInEUR.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (EUR)" + "</div"
                );
    }

    public static Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    @Subscribe
    public void onInit(InitEvent event) {
        accountRemainsesTable.setStyleProvider(new GroupTable.GroupStyleProvider<AccountRemains>() {
            @Nullable
            @Override
            public String getStyleName(AccountRemains entity, @Nullable String property) {
                if (!Objects.isNull(property) && property.equals("summEqualsUAH")) {
                    return "remains-on-date";
                }
                return null;
            }

            @Nullable
            @Override
            public String getStyleName(GroupInfo info) {
                if (info.getProperty().equals("summEqualsUAH")) {
                    return "remains-on-date";
                }
                return null;
            }
        });
    }


}