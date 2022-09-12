package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_DOCUMENT_TYPE")
@Entity(name = "finance_DocumentType")
@NamePattern("%s|name")
public class DocumentType extends StandardEntity {
    private static final long serialVersionUID = 5300182617118263558L;

    @NotNull
    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}