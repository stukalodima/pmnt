package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "FINANCE_REPARATION_OBJECT")
@Entity(name = "finance_ReparationObject")
@NamePattern("#getCaption|name,invNumber")
public class ReparationObject extends StandardEntity {
    private static final long serialVersionUID = -340785044844517788L;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "INV_NUMBER")
    private String invNumber;

    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @NotNull
    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPARATION_OBJECT_STATE_ID")
    private ReparationObjectState reparationObjectState;

    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPERTY_TYPE_ID")
    private PropertyType propertyType;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "reparationObject")
    private List<ReparationFileByReparationObject> reparationFiles;

    @Column(name = "NAME_FOR_FILE")
    private String nameForFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public ReparationObjectState getReparationObjectState() {
        return reparationObjectState;
    }

    public void setReparationObjectState(ReparationObjectState reparationObjectState) {
        this.reparationObjectState = reparationObjectState;
    }

    @SuppressWarnings("unused")
    public List<ReparationFileByReparationObject> getReparationFiles() {
        return reparationFiles;
    }

    @SuppressWarnings("unused")
    public void setReparationFiles(List<ReparationFileByReparationObject> reparationFiles) {
        this.reparationFiles = reparationFiles;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCaption() {
        return name + (invNumber == null ? "" : " (" + invNumber + ")");
    }

    public String getNameForFile() {
        return nameForFile;
    }

    public void setNameForFile(String nameForFile) {
        this.nameForFile = nameForFile;
    }
}