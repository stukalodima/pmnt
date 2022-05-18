package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_CURRENCY")
@Entity(name = "finance_Currency")
@NamePattern("%s [%s]|name,code")
public class Currency extends StandardEntity {
    private static final long serialVersionUID = -2958253090921298413L;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 3)
    private String code;

    @NotNull
    @Column(name = "SHORT_NAME", nullable = false, length = 3)
    private String shortName;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BASE_CURRENCY")
    private Boolean baseCurrency;

    public Boolean getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Boolean baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}