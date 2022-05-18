package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private Double sumInEUR;

    public Double getSumInEUR() {
        return sumInEUR;
    }

    public void setSumInEUR(Double sumInEUR) {
        this.sumInEUR = sumInEUR;
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


}