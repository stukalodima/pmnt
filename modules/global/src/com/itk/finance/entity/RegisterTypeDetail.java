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

    @Column(name = "USE_CONDITION")
    private Boolean useCondition;

    @Column(name = "CONDITION_")
    private String condition;

    @Column(name = "CONDITION_VALUE")
    private Double conditionValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REGISTER_TYPE_ID")
    private RegisterType registerType;

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Double getConditionValue() {
        return conditionValue;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(Boolean useCondition) {
        this.useCondition = useCondition;
    }

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