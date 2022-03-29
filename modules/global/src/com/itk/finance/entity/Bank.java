package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_BANK")
@Entity(name = "finance_Bank")
@NamePattern("%s [%s]|name,mfo")
public class Bank extends StandardEntity {
    private static final long serialVersionUID = -4290419560275046537L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "MFO", nullable = false, length = 6)
    private String mfo;

    @NotNull
    @Column(name = "EDRPOU", nullable = false, length = 10)
    private String edrpou;

    public String getEdrpou() {
        return edrpou;
    }

    public void setEdrpou(String edrpou) {
        this.edrpou = edrpou;
    }

    public String getMfo() {
        return mfo;
    }

    public void setMfo(String mfo) {
        this.mfo = mfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}