package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "FINANCE_COMPANY")
@Entity(name = "finance_Company")
@NamePattern("%s|shortName")
public class Company extends StandardEntity {
    private static final long serialVersionUID = -4139293635453793878L;

    @NotNull
    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;

    @NotNull
    @Column(name = "NAME", nullable = false)
    @Lob
    private String name;

    @NotNull
    @Column(name = "EDRPOU", nullable = false, length = 10)
    private String edrpou;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID")
    @NotNull
    private Business business;

    @Column(name = "INTEGRATION_ENABLE")
    private Boolean integrationEnable;

    @Column(name = "DATE_START_INTEGRATION")
    @Temporal(TemporalType.DATE)
    private Date dateStartIntegration;

    @Column(name = "NAME_FOR_FILE")
    private String nameForFile;

    public String getEdrpou() {
        return edrpou;
    }

    public void setEdrpou(String edrpou) {
        this.edrpou = edrpou;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateStartIntegration() {
        return dateStartIntegration;
    }

    public void setDateStartIntegration(Date dateStartIntegration) {
        this.dateStartIntegration = dateStartIntegration;
    }

    public Boolean getIntegrationEnable() {
        return integrationEnable;
    }

    public void setIntegrationEnable(Boolean integrationEnable) {
        this.integrationEnable = integrationEnable;
    }

    public String getNameForFile() {
        return nameForFile;
    }

    public void setNameForFile(String nameForFile) {
        this.nameForFile = nameForFile;
    }
}