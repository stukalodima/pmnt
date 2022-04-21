package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    @JoinColumn(name = "BUSSINES_ID")
    private Business bussines;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "accountRemains")
    private List<AccountRemainsDetail> accounts;

    public List<AccountRemainsDetail> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountRemainsDetail> accounts) {
        this.accounts = accounts;
    }

    public Business getBussines() {
        return bussines;
    }

    public void setBussines(Business bussines) {
        this.bussines = bussines;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }
}