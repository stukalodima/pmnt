package com.itk.finance.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_REGISTER_TYPE_DETAIL")
@Entity(name = "finance_RegisterTypeDetail")
public class RegisterTypeDetail extends StandardEntity {
    private static final long serialVersionUID = -90679518331180277L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CASH_FLOW_ITEM_ID")
    private CashFlowItem cashFlowItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REGISTER_TYPE_ID")
    private RegisterType registerType;

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public CashFlowItem getCashFlowItem() {
        return cashFlowItem;
    }

    public void setCashFlowItem(CashFlowItem cashFlowItem) {
        this.cashFlowItem = cashFlowItem;
    }
}