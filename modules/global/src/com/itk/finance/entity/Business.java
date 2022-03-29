package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_BUSINESS")
@Entity(name = "finance_Business")
@NamePattern("%s|name")
public class Business extends StandardEntity {
    private static final long serialVersionUID = -1722208945367473874L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_DIRECTOR_ID")
    @NotNull
    private User finDirector;

    @Lookup(type = LookupType.DROPDOWN, actions = {"open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MANAGEMENT_COMPANY_ID")
    private ManagementCompany managementCompany;

    public ManagementCompany getManagementCompany() {
        return managementCompany;
    }

    public void setManagementCompany(ManagementCompany managementCompany) {
        this.managementCompany = managementCompany;
    }

    public User getFinDirector() {
        return finDirector;
    }

    public void setFinDirector(User finDirector) {
        this.finDirector = finDirector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}