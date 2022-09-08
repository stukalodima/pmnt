package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "FINANCE_REPARATION_OBJECT_STATE")
@Entity(name = "finance_ReparationObjectState")
@NamePattern("%s|name")
public class ReparationObjectState extends StandardEntity {
    private static final long serialVersionUID = 3012995262230310586L;

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}