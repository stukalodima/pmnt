package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES")
@Entity(name = "finance_CashFlowItemBusinessAlternativeValues")
@NamePattern("%s (%s)|cashFlowItemBusiness, cashFlowItem")
public class CashFlowItemBusinessAlternativeValues extends StandardEntity {
    private static final long serialVersionUID = -3436808320272226607L;

    @JoinColumn(name = "CASH_FLOW_ITEM_BUSINESS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.DENY)
    private CashFlowItemBusiness cashFlowItemBusiness;

    @JoinColumn(name = "CASH_FLOW_ITEM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.DENY)
    private  CashFlowItem cashFlowItem;

    public CashFlowItemBusiness getCashFlowItemBusiness() {
        return cashFlowItemBusiness;
    }

    public void setCashFlowItemBusiness(CashFlowItemBusiness cashFlowItemBusiness) {
        this.cashFlowItemBusiness = cashFlowItemBusiness;
    }

    public CashFlowItem getCashFlowItem() {
        return cashFlowItem;
    }

    public void setCashFlowItem(CashFlowItem cashFlowItem) {
        this.cashFlowItem = cashFlowItem;
    }
}