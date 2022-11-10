package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "FINANCE_CASH_FLOW_ITEM_BUSINESS")
@Entity(name = "finance_CashFlowItemBusiness")
@NamePattern("%s|name")
public class CashFlowItemBusiness extends StandardEntity {
    private static final long serialVersionUID = 1323642813654147279L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Business business;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Company company;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CASH_FLOW_ITEM_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private CashFlowItem cashFlowItem;

    @Column(name = "CHECK_CASH_FLOW_ITEM")
    private Boolean checkCashFlowItem;

    @Composition
    @OneToMany(mappedBy = "cashFlowItemBusiness")
    @OnDelete(DeletePolicy.CASCADE)
    private List<CashFlowItemBusinessAlternativeValues> cashFlowItemBusinessAlternativeValues;

    public CashFlowItem getCashFlowItem() {
        return cashFlowItem;
    }

    public void setCashFlowItem(CashFlowItem cashFlowItem) {
        this.cashFlowItem = cashFlowItem;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Boolean getCheckCashFlowItem() {
        return checkCashFlowItem;
    }

    public void setCheckCashFlowItem(Boolean checkCashFlowItem) {
        this.checkCashFlowItem = checkCashFlowItem;
    }

    public List<CashFlowItemBusinessAlternativeValues> getCashFlowItemBusinessAlternativeValues() {
        return cashFlowItemBusinessAlternativeValues;
    }

    public void setCashFlowItemBusinessAlternativeValues(List<CashFlowItemBusinessAlternativeValues> cashFlowItemBusinessAlternativeValues) {
        this.cashFlowItemBusinessAlternativeValues = cashFlowItemBusinessAlternativeValues;
    }
}
