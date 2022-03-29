package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_MANAGEMENT_COMPANY")
@Entity(name = "finance_ManagementCompany")
@NamePattern("%s|shortName")
public class ManagementCompany extends StandardEntity {
    private static final long serialVersionUID = -6180969052876522987L;

    @NotNull
    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;

    @NotNull
    @Lob
    @Column(name = "NAME", nullable = false)
    private String name;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_CONTROLER_ID")
    @NotNull
    private User finControler;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_DIRECTOR_ID")
    @NotNull
    private User finDirector;

    public User getFinDirector() {
        return finDirector;
    }

    public void setFinDirector(User finDirector) {
        this.finDirector = finDirector;
    }

    public User getFinControler() {
        return finControler;
    }

    public void setFinControler(User finControler) {
        this.finControler = finControler;
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
}