package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.service.AccountsService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Table(name = "FINANCE_ACCOUNT_REMAINS")
@Entity(name = "finance_AccountRemains")
@PublishEntityChangedEvents
@NamePattern("%s [%s]|account,onDate")
public class AccountRemains extends StandardEntity {
    private static final long serialVersionUID = -8016082996061950193L;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "ON_DATE", nullable = false)
    private Date onDate;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "SUMM_BEFOR")
    private Double summBefor;

    @Column(name = "SUMM")
    private Double summ;

    @Column(name = "SUMM_IN_UAH")
    private Double summInUAH;

    @Column(name = "SUMM_IN_USD")
    private Double summInUSD;

    @Column(name = "SUM_IN_EUR")
    private Double summInEUR;

    @Column(name = "SUMM_EQUALS_UAH")
    private Double summEqualsUAH;

    public Double getSummEqualsUAH() {
        return summEqualsUAH;
    }

    public void setSummEqualsUAH(Double summEqualsUAH) {
        this.summEqualsUAH = summEqualsUAH;
    }

    public Double getSummInEUR() {
        return summInEUR;
    }

    public void setSummInEUR(Double sumInEUR) {
        this.summInEUR = sumInEUR;
    }

    public Double getSummInUSD() {
        return summInUSD;
    }

    public void setSummInUSD(Double summInUSD) {
        this.summInUSD = summInUSD;
    }

    public Double getSummInUAH() {
        return summInUAH;
    }

    public void setSummInUAH(Double summInUAH) {
        this.summInUAH = summInUAH;
    }

    public Double getSummBefor() {
        return summBefor;
    }

    public void setSummBefor(Double summBefor) {
        this.summBefor = summBefor;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    protected void fillSummInAllCurrency() {
        if (Objects.isNull(account)) {
            return;
        }
        AccountsService accountsService = AppBeans.get(AccountsService.class);
        DataManager dataManager = AppBeans.get(DataManager.class);
        account = dataManager.reload(account, "account-all-property");
        Currency currency = dataManager.reload(account.getCurrency(), "_local");
        if (currency.getShortName().equals("USD")) {
            summInUSD = summ;
            summInEUR = 0.;
            summInUAH = 0.;
            summEqualsUAH = summ * accountsService.getCurrentRate(onDate, currency.getShortName());
        }
        if (currency.getShortName().equals("EUR")) {
            summInEUR = summ;
            summInUSD = 0.;
            summInUAH = 0.;
            summEqualsUAH = summ * accountsService.getCurrentRate(onDate, currency.getShortName());
        }
        if (currency.getShortName().equals("UAH")) {
            summInUAH = summ;
            summInUSD = 0.;
            summInEUR = 0.;
            summEqualsUAH = summ;
        }
    }
    public void fillSummPreCommit() {
        AccountsService accountsService = AppBeans.get(AccountsService.class);
        summBefor = accountsService.getBeforSummValue(account, onDate);
        fillSummInAllCurrency();
    }

    public void setAccountIbanByCurrency(String iban, String currencyShortName) {
        AccountsService accountsService = AppBeans.get(AccountsService.class);
        account = accountsService.getCompanyAccountsByIban(iban, currencyShortName);
    }
}