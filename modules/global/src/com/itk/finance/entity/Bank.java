package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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

    @Lob
    @Column(name = "FULL_NAME")
    private String fullName;

    @NotNull
    @Column(name = "MFO", nullable = false, length = 6)
    private String mfo;

    @Column(name = "STAN")
    private String stan;

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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