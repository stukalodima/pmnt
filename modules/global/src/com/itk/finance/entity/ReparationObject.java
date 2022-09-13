package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;

@Table(name = "FINANCE_REPARATION_OBJECT")
@Entity(name = "finance_ReparationObject")
@NamePattern("#getCaption|name,invNumber")
public class ReparationObject extends StandardEntity {
    private static final long serialVersionUID = -340785044844517788L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "INV_NUMBER")
    private String invNumber;

    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPARATION_OBJECT_STATE_ID")
    private ReparationObjectState reparationObjectState;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPERTY_TYPE_ID")
    private PropertyType propertyType;

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

    public String getCaption() {
        return name + (invNumber == null ? "" : " (" + invNumber + ")");
    }
}