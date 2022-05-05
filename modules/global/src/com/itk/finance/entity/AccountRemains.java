package com.itk.finance.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "FINANCE_ACCOUNT_REMAINS")
@Entity(name = "finance_AccountRemains")
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