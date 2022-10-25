package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_BANK_GROUP")
@Entity(name = "finance_BankGroup")
@NamePattern("%s|name")
public class BankGroup extends StandardEntity {
    private static final long serialVersionUID = -1329669038780952053L;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Column(name = "NUMBER_GROUP")
    @Min(message = "{msg://finance_BankGroup.numberGroup.validation.Min}", value = 0)
    @Max(message = "{msg://finance_BankGroup.numberGroup.validation.Max}", value = 9)
    private Integer numberGroup;

    public Integer getNumberGroup() {
        return numberGroup;
    }

    public void setNumberGroup(Integer numberGroup) {
        this.numberGroup = numberGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}