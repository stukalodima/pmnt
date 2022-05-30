package com.itk.finance.web.screens;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.Currency;

import javax.inject.Inject;
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

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        onDate.setValue(timeSource.currentTimestamp());
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
}