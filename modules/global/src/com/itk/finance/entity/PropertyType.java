package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_PROPERTY_TYPE")
@Entity(name = "finance_PropertyType")
@NamePattern("%s|name")
public class PropertyType extends StandardEntity {
    private static final long serialVersionUID = 7093704493836805181L;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_FOR_FILE")
    private String nameForFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameForFile() {
        return nameForFile;
    }

    public void setNameForFile(String nameForFile) {
        this.nameForFile = nameForFile;
    }
}