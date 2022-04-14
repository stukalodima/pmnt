package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_CASH_FLOW_ITEM")
@Entity(name = "finance_CashFlowItem")
@NamePattern("%s|name")
public class CashFlowItem extends StandardEntity {
    private static final long serialVersionUID = -8111668420440215146L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "NUMBER", nullable = false)
    private Integer number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}