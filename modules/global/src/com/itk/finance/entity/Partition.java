package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_PARTITION")
@Entity(name = "finance_Partition")
@NamePattern("%s|name")
public class Partition extends StandardEntity {
    private static final long serialVersionUID = 1267199769996949717L;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_FOR_FILE")
    private String nameForFile;

    @Column(name = "USE_OBJECT")
    private Boolean useObject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUseObject() {
        return useObject;
    }

    public void setUseObject(Boolean useObject) {
        this.useObject = useObject;
    }

    public String getNameForFile() {
        return nameForFile;
    }

    public void setNameForFile(String nameForFile) {
        this.nameForFile = nameForFile;
    }
}